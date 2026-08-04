package com.newzkl.platform.plugin.audit.workflow.dag;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * 节点流转处理器
 *
 * <p>沿 DAG 后继边流转审批节点与判断节点, 判断节点经条件求值器决定分支</p>
 *
 * @author KC
 */
@Slf4j
@Component("auditPluginNodeProcessHandler")
@RequiredArgsConstructor
public class NodeProcessHandler {

    private final ConditionEvaluator conditionEvaluator;

    /**
     * 流转审批节点
     *
     * @param node 当前节点
     * @param dag 模板信息, 含节点与流转信息
     * @param contextParams 上下文参数 JSON
     * @param auditResult 审批状态
     * @return 下一节点
     * @throws PlatformException 无后继边或流转到 null 时抛出
     */
    public PEWorkflowDAG.Node processAuditNode(PEWorkflowDAG.Node node, PEWorkflowDAG dag,
                                               String contextParams, boolean auditResult) {
        WorkflowDAG workflowDAG = WorkflowDAGUtils.convert(dag);
        WorkflowDAG.Node targetNode = workflowDAG.getNode(node.getNodeId());
        Collection<PEWorkflowDAG.Edge> edges = targetNode.getSuccessorEdgeMap().values();
        if (edges.isEmpty()) {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
        PEWorkflowDAG.Node nextNode = null;
        for (PEWorkflowDAG.Edge edge : edges) {
            if (edges.size() == 1) {
                nextNode = dag.getNodeById(edge.getTo());
            } else {
                boolean property = Boolean.parseBoolean(edge.getProperty());
                if (auditResult) {
                    if (property) {
                        nextNode = dag.getNodeById(edge.getTo());
                    }
                } else {
                    if (!property) {
                        nextNode = dag.getNodeById(edge.getTo());
                    }
                }
            }
        }
        if (nextNode != null) {
            if (nextNode.getNodeType() == 1) {
                return processCheckNode(nextNode, dag, contextParams);
            } else {
                return nextNode;
            }
        } else {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
    }

    /**
     * 流转判断节点
     *
     * @param node 当前节点
     * @param dag 模板信息, 含节点与流转信息
     * @param contextParams 上下文参数 JSON
     * @return 下一节点
     * @throws PlatformException 表达式为空, 求值异常, 结果非布尔非数字, 或流转到 null 时抛出
     */
    public PEWorkflowDAG.Node processCheckNode(PEWorkflowDAG.Node node, PEWorkflowDAG dag, String contextParams) {
        boolean finalRes;
        String script = node.getNodeParams();
        if (StringUtils.isBlank(script)) {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
        HashMap<String, String> wfContext = JSON.parseObject(contextParams,
                new TypeReference<HashMap<String, String>>() {
                });
        Object result;
        try {
            result = conditionEvaluator.evaluate(script, wfContext);
        } catch (Exception e) {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
        if (result instanceof Boolean b) {
            finalRes = b;
        } else if (result instanceof Number n) {
            finalRes = n.doubleValue() > 0;
        } else {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
        WorkflowDAG workflowDAG = WorkflowDAGUtils.convert(dag);
        WorkflowDAG.Node targetNode = workflowDAG.getNode(node.getNodeId());
        Collection<PEWorkflowDAG.Edge> edges = targetNode.getSuccessorEdgeMap().values();
        if (edges.isEmpty()) {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
        PEWorkflowDAG.Node nextNode = null;
        if (edges.size() == 1) {
            nextNode = dag.getNodeById(new ArrayList<>(edges).get(0).getTo());
        }
        if (finalRes) {
            for (PEWorkflowDAG.Edge edge : edges) {
                boolean property = Boolean.parseBoolean(edge.getProperty());
                if (property) {
                    nextNode = dag.getNodeById(edge.getTo());
                }
            }
        } else {
            for (PEWorkflowDAG.Edge edge : edges) {
                boolean property = Boolean.parseBoolean(edge.getProperty());
                if (!property) {
                    nextNode = dag.getNodeById(edge.getTo());
                }
            }
        }
        if (nextNode != null) {
            if (nextNode.getNodeType() == 1) {
                return processCheckNode(nextNode, dag, contextParams);
            } else {
                return nextNode;
            }
        } else {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
    }
}
