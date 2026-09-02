package com.newzkl.platform.plugin.audit.action.cmd;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * SPU 工单审批执行命令
 *
 * @param workTableId 工单主键
 * @param skuSalePrice 审核录入的 SKU 销售价, 结构 {tempId: salePrice}, 可空
 * @author KC
 */
public record SpuWorkTableExecuteCommand(
        @NotNull Long workTableId,
        Map<String, String> skuSalePrice
) {
}
