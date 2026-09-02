package com.newzkl.platform.plugin.audit.action;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.req.SupplierQuery;
import com.newzkl.platform.base.common.ddd.model.enums.account.SupplierEnum;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;

import java.util.List;
import com.newzkl.platform.base.biz.account.model.res.SupplierAuditRes;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.RoleLimit;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.plugin.audit.action.cmd.SupplierAuditRefuseCommand;
import com.newzkl.platform.plugin.audit.action.cmd.SupplierAuditSubmitCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 供应商审核控制器
 *
 * <p>供应商审核内联模式: 审核状态直接落 supplier 主数据, 无审批流引擎、无快照、无 MQ 回调。
 * 提交/列表/详情/通过/拒绝五端点统一在 plugin-audit, 直接调用 biz-account 的
 * {@link SupplierClientDomain} 读写主数据。</p>
 *
 * @author KC
 */
@RoleLimit(client = AccountEnum.Client.ADMIN)
@RestController("auditPluginSupplierAuditController")
@RequestMapping("/audit/supplier")
@RequiredArgsConstructor
public class SupplierAuditController {

    private final SupplierClientDomain supplierClientDomain;

    /**
     * 提交供应商审核
     *
     * @param command 供应商审核提交命令
     * @return 供应商账号主键
     */
    @RoleLimit(client = AccountEnum.Client.SUPPLIER)
    @PostMapping("/submit")
    public PlatformResult<Long> submit(@RequestBody SupplierAuditSubmitCommand command) {
        Long accountId = SecurityUtils.getAccountId();
        supplierClientDomain.supplierSubmitAudit(accountId, command.companyInfo());
        return PlatformResult.success(accountId);
    }

    /**
     * 供应商审核通过
     *
     * <p>同事务直接置 supplier 主数据为已入驻+审核通过, 从主数据读取企业信息解析行业与企业名。</p>
     *
     * @param accountId 供应商账号主键
     * @return 操作结果
     */
    @PostMapping("/pass")
    public PlatformResult<String> pass(@RequestParam("accountId") Long accountId) {
        supplierClientDomain.supplierAuditPass(accountId);
        return PlatformResult.success();
    }

    /**
     * 供应商审核拒绝
     *
     * <p>同事务直接置 supplier 主数据审核状态为未通过并记录拒绝原因。</p>
     *
     * @param command 供应商审核拒绝命令
     * @return 操作结果
     */
    @PostMapping("/refuse")
    public PlatformResult<String> refuse(@Validated @RequestBody SupplierAuditRefuseCommand command) {
        supplierClientDomain.auditFail(command.accountId(), command.reason());
        return PlatformResult.success();
    }

    /**
     * 供应商审核列表
     *
     * <p>直接按 supplier 主数据的审核状态过滤分页。</p>
     *
     * @param supplierQuery 供应商查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<Page<SupplierAuditRes>> page(@RequestBody SupplierQuery supplierQuery) {
        // 审核列表排除未提交态: state 去 INIT; 前端未指定审核态时兜底去 CUSTOM
        supplierQuery.setStateList(List.of(SupplierEnum.State.AUDITING, SupplierEnum.State.NORMAL));
        if (CollUtil.isEmpty(supplierQuery.getAuditStateList())) {
            supplierQuery.setAuditStateList(List.of(AuditEnum.State.AUDITING, AuditEnum.State.SUCCESS, AuditEnum.State.FAIL));
        }
        return PlatformResult.success(supplierClientDomain.supplierAuditPage(supplierQuery));
    }

    /**
     * 供应商审核详情
     *
     * <p>直接查 supplier 主数据单行返回。</p>
     *
     * @param supplierId 供应商账号主键
     * @return 供应商详情
     */
    @GetMapping("/detail")
    public PlatformResult<SupplierAuditRes> detail(@RequestParam("supplierId") Long supplierId) {
        return PlatformResult.success(supplierClientDomain.supplierAuditDetail(supplierId));
    }
}
