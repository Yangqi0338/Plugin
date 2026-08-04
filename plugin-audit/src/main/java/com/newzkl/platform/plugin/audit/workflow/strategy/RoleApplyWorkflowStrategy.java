package com.newzkl.platform.plugin.audit.workflow.strategy;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.plugin.audit.port.AuditEventPublishPort;
import com.newzkl.platform.plugin.audit.port.RoleApplyDataPort;
import com.newzkl.platform.plugin.audit.workflow.constant.AuditEnum;
import com.newzkl.platform.plugin.audit.workflow.model.AuditAccountView;
import com.newzkl.platform.plugin.audit.workflow.model.AuditEventMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色申请审批策略
 *
 * <p>模板类型 1, 审批数据经 RoleApplyDataPort 转调 biz-account, 数据以 JSON 传递</p>
 *
 * @author KC
 */
@Service("auditPluginRoleApplyWorkflowStrategy")
@RequiredArgsConstructor
public class RoleApplyWorkflowStrategy extends WorkflowStrategy<String> {

    private final RoleApplyDataPort roleApplyDataPort;
    private final AuditEventPublishPort auditEventPublishPort;

    @Override
    public Long support() {
        return AuditEnum.TemplateType.ROLE_APPLY.getCode();
    }

    @Override
    protected String getTag() {
        return "audit-role-message";
    }

    @Override
    protected void check(Long accountId, String dataVO) {
        // 平价缺口: new-scm 此处按 accountId+roleId 计在途审批抛 EXIST_AUDIT, 端口未暴露 count, 交由 adapter 落 save 时兜底
    }

    @Override
    protected String getContextParams(String dataVO) {
        return null;
    }

    @Override
    protected Object getDataVO(Long flowId) {
        return JSON.parse(roleApplyDataPort.detail(flowId));
    }

    @Override
    public String detail(Long flowId, AuditAccountView account) {
        return roleApplyDataPort.detail(flowId);
    }

    @Override
    public Object pageJson(Long accountId, Long roleId, String pageQuery) {
        return roleApplyDataPort.pageJson(pageQuery);
    }

    @Override
    protected void saveData(Long flowId, String dataVO) {
        JSONObject obj = JSON.parseObject(dataVO);
        obj.put("flowId", flowId);
        roleApplyDataPort.save(obj.toJSONString());
    }

    @Override
    protected List<Long> getOldFlowIdList(Long flowId, Long accountId, Long roleId, String dataVO) {
        return roleApplyDataPort.oldFlowIds(accountId, roleId);
    }

    @Override
    protected void editBusinessData(Long flowId, String editCommand) {
        // 角色申请无业务数据修改, 与 new-scm 一致留空
    }

    @Override
    protected void sendAuditEvent(AuditEventMsg msg) {
        auditEventPublishPort.publish(msg.getTag(), msg);
    }
}
