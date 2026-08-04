package com.newzkl.platform.plugin.audit.workflow.strategy;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.plugin.audit.port.AuditEventPublishPort;
import com.newzkl.platform.plugin.audit.port.NameAuthDataPort;
import com.newzkl.platform.plugin.audit.workflow.constant.AuditEnum;
import com.newzkl.platform.plugin.audit.workflow.model.AuditAccountView;
import com.newzkl.platform.plugin.audit.workflow.model.AuditEventMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实名认证审批策略
 *
 * <p>模板类型 6, 审批数据经 NameAuthDataPort 转调 biz-account 实名认证一族</p>
 *
 * @author KC
 */
@Service("auditPluginNameAuthWorkflowStrategy")
@RequiredArgsConstructor
public class NameAuthWorkflowStrategy extends WorkflowStrategy<String> {

    private final NameAuthDataPort nameAuthDataPort;
    private final AuditEventPublishPort auditEventPublishPort;

    @Override
    public Long support() {
        return AuditEnum.TemplateType.NAME_AUTH.getCode();
    }

    @Override
    protected String getTag() {
        return "audit-nameauth-message";
    }

    @Override
    protected void check(Long accountId, String dataVO) {
        // 实名认证无前置校验, 与 new-scm 一致留空
    }

    @Override
    protected String getContextParams(String dataVO) {
        return null;
    }

    @Override
    protected Object getDataVO(Long flowId) {
        return JSON.parse(nameAuthDataPort.detail(flowId));
    }

    @Override
    public String detail(Long flowId, AuditAccountView account) {
        return nameAuthDataPort.detail(flowId);
    }

    @Override
    public Object pageJson(Long accountId, Long roleId, String pageQuery) {
        return nameAuthDataPort.pageJson(pageQuery);
    }

    @Override
    protected void saveData(Long flowId, String dataVO) {
        JSONObject obj = JSON.parseObject(dataVO);
        obj.put("flowId", flowId);
        nameAuthDataPort.save(obj.toJSONString());
    }

    @Override
    protected List<Long> getOldFlowIdList(Long flowId, Long accountId, Long roleId, String dataVO) {
        return nameAuthDataPort.oldFlowIds(accountId, roleId);
    }

    @Override
    protected void editBusinessData(Long flowId, String editCommand) {
        // 实名认证无业务数据修改, 与 new-scm 一致留空
    }

    @Override
    protected void sendAuditEvent(AuditEventMsg msg) {
        auditEventPublishPort.publish(msg.getTag(), msg);
    }
}
