package com.newzkl.platform.plugin.audit.action.cmd;

import io.soabase.recordbuilder.core.RecordBuilder;

/**
 * 审批执行命令
 *
 * <p>承载通过/终止动作的入参, 按 templateType 分派策略, editCommand 供通过时改业务数据</p>
 *
 * @author KC
 */
@RecordBuilder
public record AuditExecuteCommand(

        /** 审批模板类型, 工厂按此分派策略 */
        Long templateType,

        /** 审批流主键 */
        Long flowId,

        /** 业务数据修改指令 JSON, 通过时可空 */
        String editCommand,

        /** 终止/说明原因 */
        String reason
) {
}
