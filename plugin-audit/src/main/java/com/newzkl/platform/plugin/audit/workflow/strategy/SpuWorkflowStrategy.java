package com.newzkl.platform.plugin.audit.workflow.strategy;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.plugin.audit.port.AuditEventPublishPort;
import com.newzkl.platform.plugin.audit.port.SpuAuditDataPort;
import com.newzkl.platform.plugin.audit.workflow.constant.AuditEnum;
import com.newzkl.platform.plugin.audit.workflow.model.AuditAccountView;
import com.newzkl.platform.plugin.audit.workflow.model.AuditEventMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SPU 上传审批策略
 *
 * <p>模板类型 3, 审批数据经 SpuAuditDataPort 转调 biz-goods AuditDataSpu 一族</p>
 *
 * @author KC
 */
@Service("auditPluginSpuWorkflowStrategy")
@RequiredArgsConstructor
public class SpuWorkflowStrategy extends WorkflowStrategy<String> {

    private final SpuAuditDataPort spuAuditDataPort;
    private final AuditEventPublishPort auditEventPublishPort;

    @Override
    public Long support() {
        return AuditEnum.TemplateType.SPU_CREATE.getCode();
    }

    @Override
    protected String getTag() {
        return "audit-spu-message";
    }

    @Override
    protected void check(Long accountId, String dataVO) {
        // SPU 上传无前置校验, 与 new-scm 一致留空
    }

    @Override
    protected String getContextParams(String dataVO) {
        return null;
    }

    @Override
    protected Object getDataVO(Long flowId) {
        return JSON.parse(spuAuditDataPort.detail(flowId));
    }

    @Override
    public String detail(Long flowId, AuditAccountView account) {
        return spuAuditDataPort.detail(flowId);
    }

    @Override
    public Object pageJson(Long accountId, Long roleId, String pageQuery) {
        return spuAuditDataPort.pageJson(pageQuery);
    }

    @Override
    protected void saveData(Long flowId, String dataVO) {
        JSONObject obj = JSON.parseObject(dataVO);
        obj.put("flowId", flowId);
        spuAuditDataPort.save(obj.toJSONString());
    }

    @Override
    protected List<Long> getOldFlowIdList(Long flowId, Long accountId, Long roleId, String dataVO) {
        return null;
    }

    @Override
    protected void editBusinessData(Long flowId, String editCommand) {
        // SPU 上传无业务数据修改, 与 new-scm 一致留空
    }

    @Override
    protected void sendAuditEvent(AuditEventMsg msg) {
        auditEventPublishPort.publish(msg.getTag(), msg);
    }
}
