package com.newzkl.platform.plugin.audit.workflow.strategy;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import com.newzkl.platform.plugin.audit.workflow.constant.AuditEnum;
import com.newzkl.platform.plugin.audit.workflow.constant.AuditErrorCode;
import com.newzkl.platform.plugin.audit.workflow.dag.NodeProcessHandler;
import com.newzkl.platform.plugin.audit.workflow.dag.PEWorkflowDAG;
import com.newzkl.platform.plugin.audit.workflow.model.AuditAccountView;
import com.newzkl.platform.plugin.audit.workflow.model.AuditEventMsg;
import com.newzkl.platform.plugin.audit.workflow.model.AuditFlow;
import com.newzkl.platform.plugin.audit.workflow.model.AuditTemplate;
import com.newzkl.platform.plugin.audit.workflow.repository.AuditFlowRepository;
import com.newzkl.platform.plugin.audit.workflow.repository.AuditTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 审批流策略基类
 *
 * <p>模板方法: apply/pass/refuse/stop 为公共流转骨架, 业务差异由子类钩子填充</p>
 *
 * @param <T> 审批数据视图类型
 * @author KC
 */
@Slf4j
public abstract class WorkflowStrategy<T> {

    @Autowired
    private AuditFlowRepository auditFlowRepository;
    @Autowired
    private AuditTemplateRepository auditTemplateRepository;
    @Autowired
    private NodeProcessHandler nodeProcessHandler;

    /**
     * 本策略支持的审批模板类型
     *
     * @return 模板类型 code
     */
    public abstract Long support();

    /**
     * 发起审批
     *
     * @param templateId 审批模板主键
     * @param account 申请人
     * @param dataVO 审批业务数据
     * @return 新建审批流主键
     */
    @Transactional(rollbackFor = Exception.class)
    public Long apply(Long templateId, AuditAccountView account, T dataVO) {
        check(account.accountId(), dataVO);
        AuditFlow auditFlow = AuditFlow.builder()
                .id(SnowflakeGenerator.getSnowflakeId())
                .accountId(account.accountId())
                .username(account.username())
                .roleId(account.roleId())
                .templateId(templateId)
                .state(AuditEnum.State.AUDITING.getCode())
                .currentCode("start")
                .currentAuditPermissionVO(null)
                .isNew(AuditEnum.Switch.ON.getCode())
                .contextParams(getContextParams(dataVO))
                .build();
        auditFlowRepository.createAuditFlow(auditFlow);
        List<Long> oldFlowIdList = getOldFlowIdList(auditFlow.getId(), account.accountId(), account.roleId(), dataVO);
        if (ObjectUtil.isNotEmpty(oldFlowIdList)) {
            auditFlowRepository.updateIsNew(oldFlowIdList, AuditEnum.Switch.OFF.getCode());
        }
        saveData(auditFlow.getId(), dataVO);
        return auditFlow.getId();
    }

    /**
     * 审批通过
     *
     * @param flowId 审批流主键
     * @param account 审批人
     * @param editCommand 业务数据修改指令 JSON, 可空
     */
    @Transactional(rollbackFor = Exception.class)
    public void pass(Long flowId, AuditAccountView account, String editCommand) {
        AuditFlow auditFlow = auditFlowRepository.getAuditFlow(flowId);
        if (auditFlow == null) {
            throw new PlatformException(AuditErrorCode.NOT_FOUND);
        }
        if (StringUtils.isNotEmpty(editCommand)) {
            try {
                editBusinessData(flowId, editCommand);
            } catch (Exception e) {
                log.error("修改业务数据失败", e);
                throw new PlatformException(AuditErrorCode.EDIT_BUSINESS_DATA);
            }
        }
        flowToNext(auditFlow, true);
        auditFlowRepository.updateAuditFlow(auditFlow);
        if (AuditEnum.State.SUCCESS.getCode().equals(auditFlow.getState())) {
            sendAuditEvent(buildEventMsg(auditFlow));
        }
    }

    /**
     * 审批拒绝
     *
     * @param flowId 审批流主键
     * @param account 审批人
     * @param reason 拒绝原因
     */
    @Transactional(rollbackFor = Exception.class)
    public void refuse(Long flowId, AuditAccountView account, String reason) {
        AuditFlow auditFlow = auditFlowRepository.getAuditFlow(flowId);
        if (auditFlow == null) {
            throw new PlatformException(AuditErrorCode.NOT_FOUND);
        }
        flowToNext(auditFlow, false);
        auditFlow.setLastRefuseReason(reason);
        auditFlowRepository.updateAuditFlow(auditFlow);
        if (AuditEnum.State.FAIL.getCode().equals(auditFlow.getState())) {
            sendAuditEvent(buildEventMsg(auditFlow));
        }
    }

    /**
     * 审批终止
     *
     * @param flowId 审批流主键
     * @param account 审批人
     * @param reason 终止原因
     */
    public void stop(Long flowId, AuditAccountView account, String reason) {
        AuditFlow auditFlow = auditFlowRepository.getAuditFlow(flowId);
        if (auditFlow == null) {
            throw new PlatformException(AuditErrorCode.NOT_FOUND);
        }
        auditFlow.setState(AuditEnum.State.STOP.getCode());
        auditFlow.setLastRefuseReason(reason);
        sendAuditEvent(buildEventMsg(auditFlow));
        auditFlowRepository.updateAuditFlow(auditFlow);
    }

    /**
     * 沿模板 DAG 流转到下一节点并置状态
     *
     * @param auditFlow 审批流, 就地改 currentCode 与 state
     * @param auditResult 审批结果, true 通过 false 拒绝
     */
    private void flowToNext(AuditFlow auditFlow, boolean auditResult) {
        try {
            if (AuditEnum.END_STEP_CODE.equals(auditFlow.getCurrentCode())) {
                throw new PlatformException(AuditErrorCode.AUDIT_FINISHED);
            }
            AuditTemplate auditTemplate = auditTemplateRepository.getAuditTemplate(auditFlow.getTemplateId());
            PEWorkflowDAG dag = new PEWorkflowDAG();
            dag.setNodes(JSON.parseArray(auditTemplate.getNode(), PEWorkflowDAG.Node.class));
            dag.setEdges(JSON.parseArray(auditTemplate.getEdge(), PEWorkflowDAG.Edge.class));
            PEWorkflowDAG.Node currentNode = dag.getNodeByName(auditFlow.getCurrentCode());
            PEWorkflowDAG.Node nextNode = nodeProcessHandler.processAuditNode(currentNode, dag,
                    auditFlow.getContextParams(), auditResult);
            auditFlow.setCurrentCode(nextNode.getNodeName());
            if (AuditEnum.END_STEP_CODE.equals(auditFlow.getCurrentCode())) {
                auditFlow.setState(auditResult
                        ? AuditEnum.State.SUCCESS.getCode()
                        : AuditEnum.State.FAIL.getCode());
            } else {
                auditFlow.setState(AuditEnum.State.AUDITING.getCode());
            }
        } catch (PlatformException e) {
            throw e;
        } catch (Exception e) {
            log.error("流转节点失败", e);
            throw new PlatformException(AuditErrorCode.FLOW_CODE_ERROR);
        }
    }

    /**
     * 由审批流构建外发事件消息
     *
     * @param auditFlow 审批流
     * @return 事件消息, data 为业务数据 JSON, tag 为路由标签
     */
    private AuditEventMsg buildEventMsg(AuditFlow auditFlow) {
        AuditEventMsg msg = TransferUtils.transfer(auditFlow, AuditEventMsg::new);
        msg.setData(JSON.toJSONString(getDataVO(auditFlow.getId())));
        msg.setTag(getTag());
        return msg;
    }

    /**
     * 事件路由标签
     *
     * @return 路由标签
     */
    protected abstract String getTag();

    /**
     * 发起前业务校验
     *
     * @param accountId 申请人主键
     * @param dataVO 审批业务数据
     */
    protected abstract void check(Long accountId, T dataVO);

    /**
     * 构建上下文参数 JSON, 供判断节点求值
     *
     * @param dataVO 审批业务数据
     * @return 上下文参数 JSON
     */
    protected abstract String getContextParams(T dataVO);

    /**
     * 取审批业务数据, 用于外发事件
     *
     * @param flowId 审批流主键
     * @return 业务数据对象
     */
    protected abstract Object getDataVO(Long flowId);

    /**
     * 审批详情
     *
     * @param flowId 审批流主键
     * @param account 查看人
     * @return 审批业务数据视图
     */
    public abstract T detail(Long flowId, AuditAccountView account);

    /**
     * 审批分页
     *
     * @param accountId 账号主键
     * @param roleId 角色主键
     * @param pageQuery 分页查询 JSON
     * @return 分页结果
     */
    public abstract Object pageJson(Long accountId, Long roleId, String pageQuery);

    /**
     * 保存审批业务数据
     *
     * @param flowId 审批流主键
     * @param dataVO 审批业务数据
     */
    protected abstract void saveData(Long flowId, T dataVO);

    /**
     * 取同组业务下的历史审批流主键, 由子类界定何为一组
     *
     * @param flowId 当前审批流主键
     * @param accountId 账号主键
     * @param roleId 角色主键
     * @param dataVO 审批业务数据
     * @return 历史审批流主键列表
     */
    protected abstract List<Long> getOldFlowIdList(Long flowId, Long accountId, Long roleId, T dataVO);

    /**
     * 修改业务数据
     *
     * @param flowId 审批流主键
     * @param editCommand 修改指令 JSON
     */
    protected abstract void editBusinessData(Long flowId, String editCommand);

    /**
     * 外发审批事件
     *
     * @param msg 事件消息
     */
    protected abstract void sendAuditEvent(AuditEventMsg msg);
}
