package com.newzkl.platform.plugin.audit.infrastructure.repository;

import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.plugin.audit.infrastructure.dao.AuditFlowLogDAO;
import com.newzkl.platform.plugin.audit.infrastructure.entity.AuditFlowLogDO;
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

    private final AuditFlowLogDAO auditPluginFlowLogDAO;

    @Override
    public Long save(AuditFlowLog auditFlowLog) {
        AuditFlowLogDO logDO = TransferUtils.transfer(auditFlowLog, AuditFlowLogDO::new);
        auditPluginFlowLogDAO.insert(logDO);
        return logDO.getId();
    }
}
