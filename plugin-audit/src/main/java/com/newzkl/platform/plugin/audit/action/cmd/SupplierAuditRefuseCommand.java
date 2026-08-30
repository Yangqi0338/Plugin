package com.newzkl.platform.plugin.audit.action.cmd;

import jakarta.validation.constraints.NotNull;

/**
 * 供应商审核拒绝命令
 *
 * @param accountId 供应商账号主键
 * @param reason 拒绝原因
 * @author KC
 */
public record SupplierAuditRefuseCommand(@NotNull(message = "账号ID不能为空") Long accountId, String reason) {
}
