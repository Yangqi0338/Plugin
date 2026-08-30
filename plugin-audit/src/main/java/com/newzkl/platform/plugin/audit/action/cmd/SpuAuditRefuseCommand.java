package com.newzkl.platform.plugin.audit.action.cmd;

import jakarta.validation.constraints.NotNull;

/**
 * SPU审核拒绝命令
 *
 * @param spuId SPU主键
 * @param reason 拒绝原因
 */
public record SpuAuditRefuseCommand(@NotNull(message = "SPU主键不能为空") Long spuId,
                                    String reason) {
}
