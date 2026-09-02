package com.newzkl.platform.plugin.audit.action.cmd;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * SPU 工单审批拒绝命令
 *
 * @param workTableId 工单主键
 * @param reason 拒绝原因
 * @author KC
 */
public record SpuWorkTableRefuseCommand(
        @NotNull Long workTableId,
        @NotBlank String reason
) {
}
