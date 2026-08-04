package com.newzkl.platform.plugin.audit.workflow.service;

/**
 * 工单审批回调服务
 *
 * <p>审批到终态后回调, 通过则落审批人改动并按操作类型分派处理器执行业务变更</p>
 *
 * @author KC
 */
public interface WorktableApprovalService {

    /**
     * 处理工单审批结果
     *
     * @param workTableId 工单审批数据主键
     * @param state 审批终态, 取 AuditEnum.State 名称
     * @param editCommand 审批人业务数据修改指令 JSON, 可空
     */
    void approval(Long workTableId, String state, String editCommand);
}
