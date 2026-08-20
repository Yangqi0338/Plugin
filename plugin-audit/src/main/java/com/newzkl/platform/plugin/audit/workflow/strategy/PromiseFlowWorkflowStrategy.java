package com.newzkl.platform.plugin.audit.workflow.strategy;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.plugin.audit.port.AuditEventPublishPort;
import com.newzkl.platform.plugin.audit.port.PromiseFlowDataPort;
import com.newzkl.platform.plugin.audit.workflow.constant.AuditEnum;
import com.newzkl.platform.plugin.audit.workflow.model.AuditAccountView;
import com.newzkl.platform.plugin.audit.workflow.model.AuditEventMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 保证金缴纳审批策略
 *
 * <p>模板类型 2, Base 当前无保证金审批基建, 缺基建行为由 PromiseFlowDataPort 实现兜底</p>
 *
 * @author KC
 */
@Service("auditPluginPromiseFlowWorkflowStrategy")
@RequiredArgsConstructor
public class PromiseFlowWorkflowStrategy extends WorkflowStrategy<String> {

    private final PromiseFlowDataPort promiseFlowDataPort;
    private final AuditEventPublishPort auditEventPublishPort;

    @Override
    public Long support() {
        return AuditEnum.TemplateType.PROMISE_FLOW.getCode();
    }

    @Override
    protected String getTag() {
        return "audit-promise-message";
    }

    @Override
    protected void check(Long accountId, String dataVO) {
        // 保证金无前置校验
    }

    @Override
    protected String getContextParams(String dataVO) {
        return null;
    }

    @Override
    protected Object getDataVO(Long flowId) {
        return JSON.parse(promiseFlowDataPort.detail(flowId));
    }

    @Override
    public String detail(Long flowId, AuditAccountView account) {
        return promiseFlowDataPort.detail(flowId);
    }

    @Override
    public Object pageJson(Long accountId, AccountEnum.Identity identity, String pageQuery) {
        return promiseFlowDataPort.pageJson(pageQuery);
    }

    @Override
    protected void saveData(Long flowId, String dataVO) {
        JSONObject obj = JSON.parseObject(dataVO);
        obj.put("flowId", flowId);
        promiseFlowDataPort.save(obj.toJSONString());
    }

    @Override
    protected List<Long> getOldFlowIdList(Long flowId, Long accountId, AccountEnum.Identity identity, String dataVO) {
        return promiseFlowDataPort.oldFlowIds(accountId, identity);
    }

    @Override
    protected void editBusinessData(Long flowId, String editCommand) {
        // 保证金无业务数据修改
    }

    @Override
    protected void sendAuditEvent(AuditEventMsg msg) {
        auditEventPublishPort.publish(msg.getTag(), msg);
    }
}
