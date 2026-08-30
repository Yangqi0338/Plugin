package com.newzkl.platform.plugin.audit.action.cmd;

import jakarta.validation.constraints.NotNull;

/**
 * SPU审核通过命令
 *
 * @param spuId SPU主键
 * @param skuSalePriceJson SKU销售价调整 JSON (平台审核时可改价, 为空则沿用供应商提交价)
 */
public record SpuAuditPassCommand(@NotNull(message = "SPU主键不能为空") Long spuId,
                                  String skuSalePriceJson) {
}
