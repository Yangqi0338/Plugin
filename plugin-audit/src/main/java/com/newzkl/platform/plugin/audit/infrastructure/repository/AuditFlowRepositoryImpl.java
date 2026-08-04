package com.newzkl.platform.plugin.audit.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.plugin.audit.infrastructure.dao.AuditPluginFlowDAO;
import com.newzkl.platform.plugin.audit.infrastructure.dao.po.AuditPluginFlowDO;
import com.newzkl.platform.plugin.audit.workflow.model.AuditFlow;
import com.newzkl.platform.plugin.audit.workflow.repository.AuditFlowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 审批流仓储实现
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class AuditFlowRepositoryImpl implements AuditFlowRepository {

    private final AuditPluginFlowDAO auditPluginFlowDAO;

    @Override
    public void createAuditFlow(AuditFlow auditFlow) {
        AuditPluginFlowDO flowDO = TransferUtils.transfer(auditFlow, AuditPluginFlowDO::new);
        auditPluginFlowDAO.insert(flowDO);
    }

    @Override
    public AuditFlow getAuditFlow(Long flowId) {
        return TransferUtils.transfer(auditPluginFlowDAO.selectById(flowId), AuditFlow::new);
    }

    @Override
    public void updateAuditFlow(AuditFlow auditFlow) {
        auditPluginFlowDAO.updateById(TransferUtils.transfer(auditFlow, AuditPluginFlowDO::new));
    }

    @Override
    public void updateIsNew(List<Long> flowIdList, Integer isNew) {
        if (flowIdList == null || flowIdList.isEmpty()) {
            return;
        }
        LambdaUpdateWrapper<AuditPluginFlowDO> wrapper = new LambdaUpdateWrapper<AuditPluginFlowDO>()
                .in(AuditPluginFlowDO::getId, flowIdList)
                .set(AuditPluginFlowDO::getIsNew, isNew);
        auditPluginFlowDAO.update(null, wrapper);
    }
}
