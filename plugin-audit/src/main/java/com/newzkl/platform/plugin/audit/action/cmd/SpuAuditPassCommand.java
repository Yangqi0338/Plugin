package com.newzkl.platform.plugin.audit.action.cmd;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * SPU审核通过命令
 *
 * @param spuId SPU主键
 * @param skuSalePriceJson SKU销售价调整
 */
public record SpuAuditPassCommand(@NotNull Long spuId,
                                  @NotEmpty Map<Long, String> skuSalePriceJson) {
}
