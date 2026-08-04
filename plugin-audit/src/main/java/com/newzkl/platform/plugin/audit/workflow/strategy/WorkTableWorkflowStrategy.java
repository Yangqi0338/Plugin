package com.newzkl.platform.plugin.audit.workflow.strategy;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.plugin.audit.port.AuditEventPublishPort;
import com.newzkl.platform.plugin.audit.port.WorktableDataPort;
import com.newzkl.platform.plugin.audit.workflow.constant.AuditEnum;
import com.newzkl.platform.plugin.audit.workflow.constant.AuditErrorCode;
import com.newzkl.platform.plugin.audit.workflow.model.AuditAccountView;
import com.newzkl.platform.plugin.audit.workflow.model.AuditEventMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SPU 工单审批策略
 *
 * <p>模板类型 5, 审批数据经 WorktableDataPort 转调 biz-goods AuditDataWorkTable 一族</p>
 *
 * @author KC
 */
@Service("auditPluginWorkTableWorkflowStrategy")
@RequiredArgsConstructor
public class WorkTableWorkflowStrategy extends WorkflowStrategy<String> {

    /** 操作目标: 销售属性, 对齐 biz-finance SpuEnum.OperateTarget.SALE_ATTRIBUTE, 硬线禁 import 故本地定义 */
    private static final int TARGET_SALE_ATTRIBUTE = 2;

    /** 操作目标: SKU 基础信息 */
    private static final int TARGET_SKU_BASE = 4;

    /** 操作类型: 修改 */
    private static final int TYPE_UPDATE = 1;

    /** 操作类型: 新增 */
    private static final int TYPE_ADD = 2;

    private final WorktableDataPort worktableDataPort;
    private final AuditEventPublishPort auditEventPublishPort;

    @Override
    public Long support() {
        return AuditEnum.TemplateType.SPU_WORK_TABLE.getCode();
    }

    @Override
    protected String getTag() {
        return "audit-worktable-message";
    }

    @Override
    protected void check(Long accountId, String dataVO) {
        // 工单无前置校验, 与 new-scm 一致留空
    }

    @Override
    protected String getContextParams(String dataVO) {
        JSONObject obj = JSON.parseObject(dataVO);
        Integer operateTarget = obj.getInteger("operateTarget");
        Integer operateType = obj.getInteger("operateType");
        Map<String, Object> map = new HashMap<>(5);
        boolean targetHit = Integer.valueOf(TARGET_SALE_ATTRIBUTE).equals(operateTarget)
                || Integer.valueOf(TARGET_SKU_BASE).equals(operateTarget);
        boolean typeHit = Integer.valueOf(TYPE_UPDATE).equals(operateType)
                || Integer.valueOf(TYPE_ADD).equals(operateType);
        map.put("price", targetHit && typeHit ? "1" : "0");
        return JSON.toJSONString(map);
    }

    @Override
    protected Object getDataVO(Long flowId) {
        JSONObject query = new JSONObject();
        query.put("flowId", flowId);
        List<String> list = worktableDataPort.listByQuery(query.toJSONString());
        if (list == null || list.isEmpty()) {
            throw new PlatformException(AuditErrorCode.AUDIT_DATA_LOSE);
        }
        return JSON.parse(list.get(0));
    }

    @Override
    public String detail(Long flowId, AuditAccountView account) {
        JSONObject query = new JSONObject();
        query.put("flowId", flowId);
        List<String> list = worktableDataPort.listByQuery(query.toJSONString());
        if (list == null || list.isEmpty()) {
            throw new PlatformException(AuditErrorCode.AUDIT_DATA_LOSE);
        }
        return list.get(0);
    }

    @Override
    public Object pageJson(Long accountId, Long roleId, String pageQuery) {
        return worktableDataPort.pageJson(pageQuery);
    }

    @Override
    protected void saveData(Long flowId, String dataVO) {
        JSONObject obj = JSON.parseObject(dataVO);
        obj.put("flowId", flowId);
        worktableDataPort.save(obj.toJSONString());
    }

    @Override
    protected List<Long> getOldFlowIdList(Long flowId, Long accountId, Long roleId, String dataVO) {
        return null;
    }

    @Override
    protected void editBusinessData(Long flowId, String editCommand) {
        JSONObject query = new JSONObject();
        query.put("flowId", flowId);
        List<String> list = worktableDataPort.listByQuery(query.toJSONString());
        if (list == null || list.isEmpty()) {
            throw new PlatformException(AuditErrorCode.AUDIT_DATA_LOSE);
        }
        Long id = JSON.parseObject(list.get(0)).getLong("id");
        worktableDataPort.editSkuSalePrice(id, editCommand);
    }

    @Override
    protected void sendAuditEvent(AuditEventMsg msg) {
        auditEventPublishPort.publish(msg.getTag(), msg);
    }
}
