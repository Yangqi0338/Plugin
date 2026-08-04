package com.newzkl.platform.plugin.audit.workflow.repository;

import com.newzkl.platform.plugin.audit.workflow.model.AuditTemplate;

/**
 * 审批模板仓储
 *
 * @author KC
 */
public interface AuditTemplateRepository {

    /**
     * 按主键查审批模板
     *
     * @param templateId 模板主键
     * @return 审批模板, 未命中返回 null
     */
    AuditTemplate getAuditTemplate(Long templateId);
}
