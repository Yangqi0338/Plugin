package com.newzkl.platform.plugin.audit.action;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.facade.GoodsSeatFacade;
import com.newzkl.platform.base.biz.goods.application.goods.service.goods.GoodsQueryService;
import com.newzkl.platform.base.biz.goods.domain.spu.service.ExecuteLogDomain;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.biz.goods.model.goods.res.spu.SpuAuditRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.RoleLimit;
import com.newzkl.platform.base.common.ddd.facade.SpuQuery;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.plugin.audit.action.cmd.SpuAuditPassCommand;
import com.newzkl.platform.plugin.audit.action.cmd.SpuAuditRefuseCommand;
import com.newzkl.platform.plugin.audit.worktable.constant.WorktableConst;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * SPU审核控制器
 *
 * <p>SPU上传审核内联模式: 审核态直接落 spu 主数据 (auditState / lastRefuseReason), 无审批流引擎、
 * 无 audit_data_spu 快照、无 MQ 回调。提交/列表/详情/通过/拒绝/终止六端点统一在 plugin-audit,
 * 直接调用 biz-goods 的 {@link SpuDomain} 读写主数据, 商品位增减经 finance {@link GoodsSeatFacade}
 * 跨域调用, 全链同事务。</p>
 *
 * <p>逐行对齐 new-scm {@code GoodsAuditFacadeImpl.spuCreateEvent} 的副作用链, 其中三项因 Base 侧
 * 尚无对等能力未接入, 已登记 deferred-issues:
 * 供应商主数据回写 {@code supplierFacade.spuCreateAuditSuccess}、
 * 结算计划落库 {@code settleFacade.settleGoodsSave}、
 * 邀请人等级提升 {@code accountFacade.levelUp}。</p>
 *
 * @author KC
 */
@RoleLimit(client = AccountEnum.Client.ADMIN)
@RestController("auditPluginSpuAuditController")
@RequestMapping("/audit/spu")
@RequiredArgsConstructor
public class SpuAuditController {

    private final SpuDomain spuDomain;
    private final GoodsQueryService goodsQueryService;
    private final GoodsSeatFacade goodsSeatFacade;
    private final ExecuteLogDomain executeLogDomain;

    /**
     * 供应商提交SPU审核
     *
     * <p>先扣1个商品位再置 auditState=AUDITING, 商品位不足由 finance 侧抛异常拦截, 同事务回滚。
     * 供应商身份取登录态, 不信任前端传参</p>
     *
     * @param spuId SPU主键
     * @return SPU主键
     */
    @RoleLimit(client = AccountEnum.Client.SUPPLIER)
    @PostMapping("/submit")
    @Transactional(rollbackFor = Exception.class)
    public PlatformResult<Long> submit(@RequestParam("spuId") Long spuId) {
        SpuVO spuVO = requireSpu(spuId, false);
        Long supplierId = SecurityUtils.getAccountId();
        ThrowsException.isTrue(supplierId.equals(spuVO.getAccountId()), BaseErrorCode.PARAM, "只能提交本供应商的SPU");
        goodsSeatFacade.supplierSubmitSubGoodsSeat(supplierId, spuId);
        spuDomain.spuSubmit(spuId);
        return PlatformResult.success(spuId);
    }

    /**
     * 平台侧SPU审核列表
     *
     * <p>强制过滤审核态为 待审核/已通过/已拒绝, 排除 CUSTOM 未提交与 STOP 已终止两类可重编辑数据。
     * 出参为审核专用瘦出参 {@link SpuAuditRes}, 不带销量/销售额/SKU 列表等详情类冗余</p>
     *
     * @param query SPU查询条件
     * @return SPU审核出参分页
     */
    @PostMapping("/page")
    public PlatformResult<Page<SpuAuditRes>> page(@RequestBody SpuQuery query) {
        query.setAuditStateList(List.of(AuditEnum.State.AUDITING, AuditEnum.State.SUCCESS, AuditEnum.State.FAIL));
        return PlatformResult.success(spuDomain.spuAuditPage(query));
    }

    /**
     * 平台侧SPU审核详情
     *
     * @param spuId SPU主键
     * @return SPU详情, 含SKU与属性等关联数据
     */
    @GetMapping("/detail")
    public PlatformResult<SpuVO> detail(@RequestParam("spuId") Long spuId) {
        return PlatformResult.success(goodsQueryService.spuVO(spuId, true));
    }

    /**
     * 平台侧SPU审核通过
     *
     * <p>置 auditState=SUCCESS 且 state=SALE 直接上架, skuSalePriceJson 非空时按平台改价刷新
     * SKU销售价与SPU价格区间, 并留一条销售价变动执行日志</p>
     *
     * @param command SPU审核通过命令
     * @return 空
     */
    @PostMapping("/pass")
    @Transactional(rollbackFor = Exception.class)
    public PlatformResult<String> pass(@Validated @RequestBody SpuAuditPassCommand command) {
        SpuVO spuVO = requireSpu(command.spuId(), true);
        spuDomain.spuAuditSuccess(spuVO, command.skuSalePriceJson());
        executeLogDomain.executeLogSave(WorktableConst.EXECUTE_TYPE_SPU_SALE_PRICE, command.spuId(),
                SecurityUtils.getUsername(), JSON.toJSONString(spuVO), command.skuSalePriceJson());
        return PlatformResult.success();
    }

    /**
     * 平台侧SPU审核拒绝
     *
     * <p>置 auditState=FAIL 并落拒绝原因, 同时返还供应商1个商品位</p>
     *
     * @param command SPU审核拒绝命令
     * @return 空
     */
    @PostMapping("/refuse")
    @Transactional(rollbackFor = Exception.class)
    public PlatformResult<String> refuse(@Validated @RequestBody SpuAuditRefuseCommand command) {
        SpuVO spuVO = requireSpu(command.spuId(), false);
        spuDomain.spuAuditFail(spuVO, command.reason());
        goodsSeatFacade.goodsAuditFailAddGoodsSeat(spuVO.getAccountId());
        return PlatformResult.success();
    }

    /**
     * 平台侧终止SPU审核
     *
     * <p>置 auditState=STOP 使供应商可重新编辑提交, 同时返还供应商1个商品位</p>
     *
     * @param spuId SPU主键
     * @return 空
     */
    @PostMapping("/stop")
    @Transactional(rollbackFor = Exception.class)
    public PlatformResult<String> stop(@RequestParam("spuId") Long spuId) {
        SpuVO spuVO = requireSpu(spuId, false);
        spuDomain.spuAuditStop(spuVO);
        goodsSeatFacade.goodsAuditFailAddGoodsSeat(spuVO.getAccountId());
        return PlatformResult.success();
    }

    /**
     * 读取SPU并校验存在
     *
     * @param spuId SPU主键
     * @param needExtraInfo 是否加载SKU与属性等关联数据
     * @return SPU视图
     */
    private SpuVO requireSpu(Long spuId, Boolean needExtraInfo) {
        SpuVO spuVO = goodsQueryService.spuVO(spuId, needExtraInfo);
        ThrowsException.isTrue(spuVO != null, BaseErrorCode.NODATA, "SPU");
        return spuVO;
    }
}
