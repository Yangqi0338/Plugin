package com.newzkl.platform.plugin.audit.workflow.repository;

import com.newzkl.platform.plugin.audit.workflow.model.AuditFlow;

import java.util.List;

/**
 * 审批流仓储
 *
 * @author KC
 */
public interface AuditFlowRepository {

    /**
     * 创建审批流
     *
     * @param auditFlow 审批流
     */
    void createAuditFlow(AuditFlow auditFlow);

    /**
     * 按主键查审批流
     *
     * @param flowId 审批单主键
     * @return 审批流, 未命中返回 null
     */
    AuditFlow getAuditFlow(Long flowId);

    /**
     * 更新审批流
     *
     * @param auditFlow 审批流
     */
    void updateAuditFlow(AuditFlow auditFlow);

    /**
     * 批量更新是否最新标记
     *
     * @param flowIdList 审批单主键列表
     * @param isNew 是否最新 1 是 0 否
     */
    void updateIsNew(List<Long> flowIdList, Integer isNew);
}
