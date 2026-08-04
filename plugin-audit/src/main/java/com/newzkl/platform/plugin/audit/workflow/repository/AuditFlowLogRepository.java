package com.newzkl.platform.plugin.audit.workflow.repository;

import com.newzkl.platform.plugin.audit.workflow.model.AuditFlowLog;

/**
 * 审批日志仓储
 *
 * @author KC
 */
public interface AuditFlowLogRepository {

    /**
     * 保存审批日志
     *
     * @param auditFlowLog 审批日志
     * @return 日志主键
     */
    Long save(AuditFlowLog auditFlowLog);
}
