package com.newzkl.platform.plugin.audit.infrastructure.repository;

import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.plugin.audit.infrastructure.dao.AuditPluginTemplateDAO;
import com.newzkl.platform.plugin.audit.workflow.model.AuditTemplate;
import com.newzkl.platform.plugin.audit.workflow.repository.AuditTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 审批模板仓储实现
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class AuditTemplateRepositoryImpl implements AuditTemplateRepository {

    private final AuditPluginTemplateDAO auditPluginTemplateDAO;

    @Override
    public AuditTemplate getAuditTemplate(Long templateId) {
        return TransferUtils.transfer(auditPluginTemplateDAO.selectById(templateId), AuditTemplate::new);
    }
}
