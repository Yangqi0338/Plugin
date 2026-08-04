package com.newzkl.platform.plugin.audit.workflow.strategy;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.plugin.audit.port.AuditEventPublishPort;
import com.newzkl.platform.plugin.audit.port.BrandAuditDataPort;
import com.newzkl.platform.plugin.audit.workflow.constant.AuditEnum;
import com.newzkl.platform.plugin.audit.workflow.model.AuditAccountView;
import com.newzkl.platform.plugin.audit.workflow.model.AuditEventMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 品牌申请审批策略
 *
 * <p>模板类型 4, Base 当前无品牌审批基建, 缺基建行为由 BrandAuditDataPort 实现兜底</p>
 *
 * @author KC
 */
@Service("auditPluginBrandWorkflowStrategy")
@RequiredArgsConstructor
public class BrandWorkflowStrategy extends WorkflowStrategy<String> {

    private final BrandAuditDataPort brandAuditDataPort;
    private final AuditEventPublishPort auditEventPublishPort;

    @Override
    public Long support() {
        return AuditEnum.TemplateType.BRAND_CREATE.getCode();
    }

    @Override
    protected String getTag() {
        return "audit-brand-message";
    }

    @Override
    protected void check(Long accountId, String dataVO) {
        // 品牌申请无前置校验
    }

    @Override
    protected String getContextParams(String dataVO) {
        return null;
    }

    @Override
    protected Object getDataVO(Long flowId) {
        return JSON.parse(brandAuditDataPort.detail(flowId));
    }

    @Override
    public String detail(Long flowId, AuditAccountView account) {
        return brandAuditDataPort.detail(flowId);
    }

    @Override
    public Object pageJson(Long accountId, Long roleId, String pageQuery) {
        return brandAuditDataPort.pageJson(pageQuery);
    }

    @Override
    protected void saveData(Long flowId, String dataVO) {
        JSONObject obj = JSON.parseObject(dataVO);
        obj.put("flowId", flowId);
        brandAuditDataPort.save(obj.toJSONString());
    }

    @Override
    protected List<Long> getOldFlowIdList(Long flowId, Long accountId, Long roleId, String dataVO) {
        return brandAuditDataPort.oldFlowIds(accountId, roleId);
    }

    @Override
    protected void editBusinessData(Long flowId, String editCommand) {
        // 品牌申请无业务数据修改
    }

    @Override
    protected void sendAuditEvent(AuditEventMsg msg) {
        auditEventPublishPort.publish(msg.getTag(), msg);
    }
}
