package com.newzkl.platform.plugin.audit.infrastructure.repository;

import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.plugin.audit.infrastructure.dao.AuditPluginFlowLogDAO;
import com.newzkl.platform.plugin.audit.infrastructure.dao.po.AuditPluginFlowLogDO;
import com.newzkl.platform.plugin.audit.workflow.model.AuditFlowLog;
import com.newzkl.platform.plugin.audit.workflow.repository.AuditFlowLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 审批日志仓储实现
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class AuditFlowLogRepositoryImpl implements AuditFlowLogRepository {

    private final AuditPluginFlowLogDAO auditPluginFlowLogDAO;

    @Override
    public Long save(AuditFlowLog auditFlowLog) {
        AuditPluginFlowLogDO logDO = TransferUtils.transfer(auditFlowLog, AuditPluginFlowLogDO::new);
        auditPluginFlowLogDAO.insert(logDO);
        return logDO.getId();
    }
}
