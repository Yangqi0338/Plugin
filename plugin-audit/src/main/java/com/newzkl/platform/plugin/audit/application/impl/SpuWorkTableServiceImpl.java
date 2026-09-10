package com.newzkl.platform.plugin.audit.application.impl;

import cn.hutool.core.map.MapUtil;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SkuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuDTO;
import com.newzkl.platform.base.biz.market.facade.DistributionFacade;
import com.newzkl.platform.base.biz.market.facade.model.UpDownReq;
import com.newzkl.platform.base.biz.market.model.req.distribution.DistributionsBatchUpdateReq;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.constant.SupplierErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.plugin.audit.application.SpuWorkTableService;
import com.newzkl.platform.plugin.audit.domain.SpuWorkTableDomain;
import com.newzkl.platform.plugin.audit.model.dto.SkuAuditDTO;
import com.newzkl.platform.plugin.audit.model.dto.SpuAuditDTO;
import com.newzkl.platform.plugin.audit.model.dto.SpuWorkTableDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * SPU 工单审批应用服务实现
 *
 * @author KC
 */
@Service("auditPluginSpuWorkTableService")
@RequiredArgsConstructor
public class SpuWorkTableServiceImpl implements SpuWorkTableService {

    private final SpuWorkTableDomain spuWorkTableDomain;
    private final SpuDomain spuDomain;
    private final DistributionFacade distributionFacade;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pass(Long workTableId, Map<String, String> skuSalePrice) {
        SpuWorkTableDTO current = spuWorkTableDomain.workTable(workTableId);
        if (current == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "工单");
        }
        // 仅待审核单可通过, 防越态/重复执行
        if (current.getAuditState() != AuditEnum.State.AUDITING) {
            throw new PlatformException(SupplierErrorCode.AUDIT_STATE);
        }
        // 按工单类型分派执行 SPU 主数据真实变更
        execute(current, skuSalePrice);
        // 变更成功后更新工单状态并落审核价快照
        spuWorkTableDomain.markPass(workTableId, skuSalePrice);
    }

    @Override
    public void refuse(Long workTableId, String reason) {
        spuWorkTableDomain.refuse(workTableId, reason);
    }
	
	/**
     * 按 operateTarget + operateType 分派执行 SPU 主数据变更
     *
     * @param workTable 工单审批数据
     * @param skuSalePrice 审核录入的 SKU 销售价, 结构 {tempId: salePrice}, 可空
     */
    private void execute(SpuWorkTableDTO workTable, Map<String, String> skuSalePrice) {
        SpuEnum.OperateTarget target = workTable.getOperateTarget();
        SpuEnum.OperateType type = workTable.getOperateType();
        SpuAuditDTO cmd = workTable.getSpuEditInfo();
        Long spuId = cmd.getId();
        switch (target) {
            case SPU_BASE -> {
                // 仅改主体信息, 置空 sku/规格/状态
                cmd.setSkuList(null);
                cmd.setSpuSaleAttributeList(null);
                cmd.setState(null);
                spuDomain.spuUpdate(TransferUtils.transfer(cmd, SpuDTO.class));
            }
            case SPU_STATE -> {
                CommonEnum.YesOrNo enable = SpuEnum.State.SALE == cmd.getState()
                        ? CommonEnum.YesOrNo.YES : CommonEnum.YesOrNo.NO;
                spuDomain.spuUp(enable, Collections.singletonList(spuId));
            }
            case SKU_BASE -> {
                // 先按 tempId 回填审核价, 再转 SpuDTO, 否则回填打在转换前的副本上丢价
                applySalePrice(cmd.getSkuList(), skuSalePrice);
                SpuDTO spuDTO = new SpuDTO();
                spuDTO.setId(spuId);
                spuDTO.setSkuList(TransferUtils.transfers(cmd.getSkuList(), SkuDTO.class));
                spuDomain.spuUpdate(spuDTO);
                distribution(spuId);
            }
            case PARAM_ATTRIBUTE -> {
                SpuDTO spuDTO = new SpuDTO();
                spuDTO.setId(spuId);
                spuDTO.setSpuParamAttributeList(cmd.getSpuParamAttributeList());
                spuDomain.spuUpdate(spuDTO);
                distribution(spuId);
            }
            case SALE_ATTRIBUTE -> {
                SpuDTO spuDTO = new SpuDTO();
                spuDTO.setId(spuId);
                spuDTO.setSpuSaleAttributeList(cmd.getSpuSaleAttributeList());
                if (SpuEnum.OperateType.ADD.equals(type)) {
                    applySalePrice(cmd.getSkuList(), skuSalePrice);
                    distribution(spuId);
                }
                spuDTO.setSkuList(TransferUtils.transfers(cmd.getSkuList(), SkuDTO.class));
                spuDomain.spuUpdate(spuDTO);
            }
            default -> throw new PlatformException(BaseErrorCode.PARAM, "未支持的工单类型: operateTarget=" + target);
        }
    }

    /**
     * 触发上下架铺货事件
     *
     * @param spuId SPU 主键
     */
    private void distribution(Long spuId) {
        UpDownReq event = new UpDownReq();
        event.setEnable(SpuEnum.State.PLATFORM_DOWN);
        event.setSpuIdList(Collections.singletonList(spuId));
        distributionFacade.upDownEvent(event);
    }

    /**
     * 按 tempId 回填审核时确定的销售价
     *
     * @param skuList 待回填的 SKU 列表, 就地改
     * @param salePrice 审核录入的价格, 结构 {tempId: salePrice}
     */
    private void applySalePrice(List<SkuAuditDTO> skuList, Map<String, String> salePrice) {
        for (SkuAuditDTO sku : skuList) {
            String price = MapUtil.getStr(salePrice, sku.getTempId());
            if (price != null) {
                sku.setSalePrice(Money.of(price));
            }
        }
    }
}
