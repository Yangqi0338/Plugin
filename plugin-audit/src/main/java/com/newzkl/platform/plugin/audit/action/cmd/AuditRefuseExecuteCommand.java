package com.newzkl.platform.plugin.audit.action.cmd;

import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.NotBlank;

/**
 * 审批拒绝执行命令
 *
 * <p>承载拒绝动作的入参, reason 必填以留痕拒绝理由</p>
 *
 * @author KC
 */
@RecordBuilder
public record AuditRefuseExecuteCommand(

        /** 审批模板类型, 工厂按此分派策略 */
        Long templateType,

        /** 审批流主键 */
        Long flowId,

        /** 拒绝原因, 必填 */
        @NotBlank(message = "拒绝原因不能为空")
        String reason
) {
}
