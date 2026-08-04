package com.newzkl.platform.plugin.audit.workflow.service;

import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.plugin.audit.port.AdminAccountPort;
import com.newzkl.platform.plugin.audit.port.SpuReadPort;
import com.newzkl.platform.plugin.audit.workflow.constant.AuditEnum;
import com.newzkl.platform.plugin.audit.workflow.model.AuditAccountView;
import com.newzkl.platform.plugin.audit.workflow.strategy.WorkflowFactory;
import com.newzkl.platform.plugin.audit.workflow.strategy.WorkflowStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工单提交编排服务
 *
 * <p>逐 SPU 组工单审批数据并起审批流, 恢复 new-scm 经审批流语义, 替代 Base 直写仓储的临时降级</p>
 *
 * @author KC
 */
@Service("auditPluginWorktableSubmitService")
@RequiredArgsConstructor
public class WorktableSubmitService {

    private final WorkflowFactory workflowFactory;
    private final AdminAccountPort adminAccountPort;
    private final SpuReadPort spuReadPort;

    /**
     * 提交工单
     *
     * <p>对 spuIdList 逐个组审批数据 JSON, 经模板类型 5 的审批策略 apply 起审批流</p>
     *
     * @param spuIdList 目标 SPU 主键列表
     * @param operateTarget 操作目标 1~5
     * @param operateType 操作类型 1 修改 2 新增 3 删除
     * @param editInfoJson 用户提交的变更 JSON, 落 spuEditInfoJson
     */
    public void submitWorkTable(List<Long> spuIdList, Integer operateTarget, Integer operateType, String editInfoJson) {
        if (spuIdList == null || spuIdList.isEmpty()) {
            throw new PlatformException(BaseErrorCode.PARAM, "spuId缺少");
        }
        AuditAccountView account = adminAccountPort.currentAccount();
        Long templateId = AuditEnum.TemplateType.SPU_WORK_TABLE.getCode();
        @SuppressWarnings("unchecked")
        WorkflowStrategy<String> policy = (WorkflowStrategy<String>) workflowFactory.getPolicy(templateId);
        for (Long spuId : spuIdList) {
            JSONObject dataVO = new JSONObject();
            dataVO.put("spuId", spuId);
            dataVO.put("spuName", spuReadPort.spuName(spuId));
            dataVO.put("operateTarget", operateTarget);
            dataVO.put("operateType", operateType);
            dataVO.put("accountId", account.accountId());
            dataVO.put("spuInfoJson", spuReadPort.spuInfoJson(spuId));
            dataVO.put("spuEditInfoJson", editInfoJson);
            policy.apply(templateId, account, dataVO.toJSONString());
        }
    }
}
