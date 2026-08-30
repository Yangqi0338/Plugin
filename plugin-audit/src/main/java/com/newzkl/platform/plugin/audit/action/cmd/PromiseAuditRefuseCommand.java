package com.newzkl.platform.plugin.audit.action.cmd;

import jakarta.validation.constraints.NotNull;

/**
 * 保证金审核拒绝命令
 *
 * @param promiseFlowId 保证金流水单主键
 * @param reason        拒绝原因
 * @author KC
 */
public record PromiseAuditRefuseCommand(
        @NotNull(message = "保证金流水单ID不能为空") Long promiseFlowId,
        String reason) {
}
