package com.newzkl.platform.plugin.audit.domain.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.application.goods.service.goods.GoodsQueryService;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.constant.SupplierErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.plugin.audit.domain.SpuWorkTableDomain;
import com.newzkl.platform.plugin.audit.domain.adapt.repository.SpuWorkTableRepository;
import com.newzkl.platform.plugin.audit.model.dto.SkuAuditDTO;
import com.newzkl.platform.plugin.audit.model.dto.SpuAuditDTO;
import com.newzkl.platform.plugin.audit.model.dto.SpuWorkTableDTO;
import com.newzkl.platform.plugin.audit.model.query.SpuWorkTableQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * SPU 工单审批领域服务实现
 *
 * <p>提交编排逐 SPU 组工单并落库为待审核; 审核态直接落 spu_work_table 主数据。SPU 主数据真实变更由
 * 应用层 SpuWorkTableService 审批通过时直接调用 biz-goods SpuDomain 执行, 本类只管工单本身生命周期。</p>
 *
 * @author KC
 */
@Service("auditPluginSpuWorkTableDomain")
@RequiredArgsConstructor
public class SpuWorkTableDomainImpl implements SpuWorkTableDomain {

    private final SpuWorkTableRepository spuWorkTableRepository;
    private final GoodsQueryService goodsQueryService;

    @Override
    public void submitSpuBaseUpdate(SpuAuditDTO spuVO) {
        submit(Collections.singletonList(spuVO.getId()),
                SpuEnum.OperateTarget.SPU_BASE, SpuEnum.OperateType.UPDATE, spuVO);
    }

    @Override
    public void submitSkuBaseUpdate(Long spuId, List<SkuAuditDTO> skuList) {
        for (SkuAuditDTO sku : skuList) {
            sku.setTempId(SnowflakeGenerator.getSnowflakeId() + "");
        }
        SpuAuditDTO editInfo = new SpuAuditDTO();
        editInfo.setSkuList(skuList);
        submit(Collections.singletonList(spuId),
                SpuEnum.OperateTarget.SKU_BASE, SpuEnum.OperateType.UPDATE, editInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitSpuStateUpdate(List<Long> spuIdList, CommonEnum.YesOrNo enable) {
        SpuAuditDTO editInfo = new SpuAuditDTO();
        editInfo.setState(enable == CommonEnum.YesOrNo.YES ? SpuEnum.State.SALE : SpuEnum.State.DOWN);
        submit(spuIdList, SpuEnum.OperateTarget.SPU_STATE, SpuEnum.OperateType.UPDATE, editInfo);
    }

    @Override
    public void submitSaleAttributeDelete(Long spuId, List<SpuAttributeVO> spuSaleAttributeList) {
        SpuAuditDTO editInfo = new SpuAuditDTO();
        editInfo.setSpuSaleAttributeList(spuSaleAttributeList);
        submit(Collections.singletonList(spuId),
                SpuEnum.OperateTarget.SALE_ATTRIBUTE, SpuEnum.OperateType.DELETE, editInfo);
    }

    @Override
    public void submitSaleAttributeAdd(Long spuId, List<SkuAuditDTO> skuList, List<SpuAttributeVO> spuSaleAttributeList) {
        SpuVO oldSpu = goodsQueryService.spuVO(spuId);
        checkSaleAttributeAdd(oldSpu, skuList, spuSaleAttributeList);
        for (SkuAuditDTO sku : skuList) {
            sku.setTempId(SnowflakeGenerator.getSnowflakeId() + "");
        }
        SpuAuditDTO editInfo = new SpuAuditDTO();
        editInfo.setSkuList(skuList);
        editInfo.setSpuSaleAttributeList(spuSaleAttributeList);
        submit(Collections.singletonList(spuId),
                SpuEnum.OperateTarget.SALE_ATTRIBUTE, SpuEnum.OperateType.ADD, editInfo);
    }

    @Override
    public Page<SpuWorkTableDTO> workTablePage(SpuWorkTableQuery query) {
        return spuWorkTableRepository.workTablePage(query);
    }

    @Override
    public SpuWorkTableDTO workTable(Long workTableId) {
        return spuWorkTableRepository.workTable(workTableId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markPass(Long workTableId, Map<String, String> skuSalePrice) {
        SpuWorkTableDTO edit = new SpuWorkTableDTO();
        edit.setId(workTableId);
        edit.setAuditState(AuditEnum.State.SUCCESS);
        if (skuSalePrice != null && !skuSalePrice.isEmpty()) {
            edit.setSkuSalePrice(skuSalePrice);
        }
        spuWorkTableRepository.workTableEdit(edit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refuse(Long workTableId, String refuseReason) {
        SpuWorkTableDTO current = spuWorkTableRepository.workTable(workTableId);
        if (current == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "工单");
        }
        // 仅待审核单可拒绝
        if (current.getAuditState() != AuditEnum.State.AUDITING) {
            throw new PlatformException(SupplierErrorCode.AUDIT_STATE);
        }
        SpuWorkTableDTO edit = new SpuWorkTableDTO();
        edit.setId(workTableId);
        edit.setAuditState(AuditEnum.State.FAIL);
        edit.setAuditRefuseReason(refuseReason);
        spuWorkTableRepository.workTableEdit(edit);
    }
    
    @Override
    public void stop(Long workTableId, String reason) {
        SpuWorkTableDTO current = spuWorkTableRepository.workTable(workTableId);
        if (current == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "工单");
        }
        // 仅待审核单可拒绝
        if (current.getAuditState() != AuditEnum.State.AUDITING) {
            throw new PlatformException(SupplierErrorCode.AUDIT_STATE);
        }
        SpuWorkTableDTO edit = new SpuWorkTableDTO();
        edit.setId(workTableId);
        edit.setAuditState(AuditEnum.State.STOP);
        edit.setAuditRefuseReason(reason);
        spuWorkTableRepository.workTableEdit(edit);
    }
    
    /**
     * 逐 SPU 组工单审批数据并落库为待审核
     *
     * <p>editInfo 为类型化模板, 逐 SPU 深拷贝后回填 id, 保证每工单 spuEditInfo 落各自 spuId</p>
     *
     * @param spuIdList 目标 SPU 主键列表
     * @param operateTarget 操作目标
     * @param operateType 操作类型
     * @param editInfo 各 SPU 共用的变更体, id 由本方法逐工单回填
     */
    private void submit(List<Long> spuIdList, SpuEnum.OperateTarget operateTarget, SpuEnum.OperateType operateType, SpuAuditDTO editInfo) {
        if (spuIdList == null || spuIdList.isEmpty()) {
            throw new PlatformException(BaseErrorCode.PARAM, "spuId缺少");
        }
        Long accountId = SecurityUtils.getAccountId();
        for (Long spuId : spuIdList) {
            SpuVO spuVO = goodsQueryService.spuVO(spuId);
            
            editInfo.setId(spuId);
            SpuWorkTableDTO workTable = new SpuWorkTableDTO();
            workTable.setAccountId(accountId);
            workTable.setSpuId(spuId);
            workTable.setSpuName(spuVO.getName());
            workTable.setOperateTarget(operateTarget);
            workTable.setOperateType(operateType);
            workTable.setSpuInfo(spuVO);
            workTable.setSpuEditInfo(editInfo);
            workTable.setAuditState(AuditEnum.State.AUDITING);
            spuWorkTableRepository.workTableSave(workTable);
        }
    }

    /**
     * 规格新增提交前笛卡尔积校验
     *
     * <p>新旧销售属性各自取值个数连乘得笛卡尔积, 差值即应新增 SKU 数, 比对当前待新增(id 为空)条数</p>
     *
     * @param oldSpu 旧 SPU 快照
     * @param skuList 待新增 SKU 列表
     * @param newAttrs 新销售属性列表
     */
    private void checkSaleAttributeAdd(SpuVO oldSpu, List<SkuAuditDTO> skuList, List<SpuAttributeVO> newAttrs) {
        List<SpuAttributeVO> oldAttrs = oldSpu == null ? Collections.emptyList() : oldSpu.getSpuSaleAttributeList();
        int needAddSkuNum = cartesian(newAttrs) - cartesian(oldAttrs);
        int currentAddSkuNum = 0;
        for (SkuAuditDTO sku : skuList) {
            if (sku.getId() == null) {
                currentAddSkuNum++;
            }
        }
        if (needAddSkuNum != currentAddSkuNum) {
            throw new PlatformException(BaseErrorCode.PARAM, "新增SKU数量不对,应为:" + needAddSkuNum);
        }
    }

    /**
     * 销售属性列表笛卡尔积基数
     *
     * @param attrs 销售属性列表, 每项 value 为取值 JSON 数组字符串
     * @return 各属性取值个数连乘
     */
    private int cartesian(List<SpuAttributeVO> attrs) {
        int num = 1;
        for (SpuAttributeVO attr : attrs) {
            num = num * JSON.parseArray(attr.getValue()).size();
        }
        return num;
    }
}
