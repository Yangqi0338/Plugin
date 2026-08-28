package com.newzkl.platform.plugin.audit.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.finance.facade.PurseFacade;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.constant.SupplierErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.plugin.audit.workflow.model.PromiseFlow;
import com.newzkl.platform.plugin.audit.workflow.model.PromiseFlowQuery;
import com.newzkl.platform.plugin.audit.workflow.repository.PromiseFlowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 保证金流水领域服务实现
 *
 * <p>保证金审核第二线内联落地: 审核态直接落 promise_flow 主数据。保证金账户充值与供应商配置读取
 * 经 finance {@link PurseFacade} 跨域调用, 本类不含持久层以外的 finance 逻辑</p>
 *
 * @author KC
 */
@Service("auditPluginPromiseFlowDomain")
@RequiredArgsConstructor
public class PromiseFlowDomainImpl implements PromiseFlowDomain {

    private final PromiseFlowRepository promiseFlowRepository;
    private final PurseFacade purseFacade;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitPromiseFlow(PromiseFlow promiseFlow) {
        if (promiseFlow.getAccountId() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "账号ID不能为空");
        }
        // 读供应商全局配置决定是否跳过人工审核
        boolean skip = purseFacade.skipPromiseAudit();
        // 跳过审核: 建单即通过并直接充值; 否则建单为待审核
        promiseFlow.setAuditState(skip ? AuditEnum.State.SUCCESS : AuditEnum.State.AUDITING);
        Long flowId = promiseFlowRepository.promiseFlowSave(promiseFlow);
        if (skip) {
            purseFacade.promiseRecharge(promiseFlow.getAccountId(), promiseFlow.getAmount());
        }
        return flowId;
    }

    @Override
    public Page<PromiseFlow> promiseFlowPage(PromiseFlowQuery query) {
        return promiseFlowRepository.promiseFlowPage(query);
    }

    @Override
    public PromiseFlow promiseFlow(Long promiseFlowId) {
        return promiseFlowRepository.promiseFlow(promiseFlowId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Money promisePass(Long promiseFlowId) {
        PromiseFlow current = promiseFlowRepository.promiseFlow(promiseFlowId);
        if (current == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "保证金流水");
        }
        // 仅待审核单可通过, 防越态/重复入账
        if (current.getAuditState() != AuditEnum.State.AUDITING) {
            throw new PlatformException(SupplierErrorCode.AUDIT_STATE);
        }
        PromiseFlow edit = new PromiseFlow();
        edit.setId(promiseFlowId);
        edit.setAuditState(AuditEnum.State.SUCCESS);
        promiseFlowRepository.promiseFlowEdit(edit);
        // 给供应商保证金账户充值 (增额 + 落动账记录), finance 侧一体完成
        purseFacade.promiseRecharge(current.getAccountId(), current.getAmount());
        return current.getAmount();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void promiseRefuse(Long promiseFlowId, String refuseReason) {
        PromiseFlow current = promiseFlowRepository.promiseFlow(promiseFlowId);
        if (current == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "保证金流水");
        }
        // 仅待审核单可拒绝
        if (current.getAuditState() != AuditEnum.State.AUDITING) {
            throw new PlatformException(SupplierErrorCode.AUDIT_STATE);
        }
        PromiseFlow edit = new PromiseFlow();
        edit.setId(promiseFlowId);
        edit.setAuditState(AuditEnum.State.FAIL);
        edit.setAuditRefuseReason(refuseReason);
        promiseFlowRepository.promiseFlowEdit(edit);
    }
}
