package com.newzkl.platform.plugin.audit.action.cmd;

import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.plugin.audit.model.dto.SkuAuditDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 规格新增工单提交入参
 *
 * <p>落工单 spuEditInfo 的 skuList 与 spuSaleAttributeList, 提交前做笛卡尔积校验,
 * 校验通过再为待新增 SKU 补 tempId, 供审核录价按 tempId 回填</p>
 *
 * @param spuId SPU 主键
 * @param skuList 新增 SKU 列表
 * @param spuSaleAttributeList 新增后完整销售属性列表
 */
public record SaleAttributeAddRequest(
        @NotNull Long spuId,
        @NotEmpty List<SkuAuditDTO> skuList,
        @NotEmpty List<SpuAttributeVO> spuSaleAttributeList) {
}
