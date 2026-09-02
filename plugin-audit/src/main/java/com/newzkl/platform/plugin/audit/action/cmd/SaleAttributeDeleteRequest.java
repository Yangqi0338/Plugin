package com.newzkl.platform.plugin.audit.action.cmd;

import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 规格删除工单提交入参
 *
 * @param spuId SPU 主键
 * @param spuSaleAttributeList 删除后保留的销售属性列表
 */
public record SaleAttributeDeleteRequest(
        @NotNull Long spuId,
        @NotEmpty List<SpuAttributeVO> spuSaleAttributeList) {
}
