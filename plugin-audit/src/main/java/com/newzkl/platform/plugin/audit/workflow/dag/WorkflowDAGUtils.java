package com.newzkl.platform.plugin.audit.workflow.dag;

import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DAG 工具类
 *
 * <p>点线表示法与引用表示法互转, Guava 集合替换为 JDK 集合</p>
 *
 * @author KC
 */
public final class WorkflowDAGUtils {

    private WorkflowDAGUtils() {
    }

    /**
     * 将点线表示法 DAG 转为引用表示法 DAG
     *
     * @param peWorkflowDAG 点线表示法 DAG
     * @return 引用表示法 DAG
     * @throws PlatformException 节点为空, 边指向不存在节点, 或无顶点时抛出
     */
    public static WorkflowDAG convert(PEWorkflowDAG peWorkflowDAG) {
        Set<Long> rootIds = new HashSet<>();
        Map<Long, WorkflowDAG.Node> id2Node = new HashMap<>();

        if (peWorkflowDAG.getNodes() == null || peWorkflowDAG.getNodes().isEmpty()) {
            throw new PlatformException(BaseErrorCode.PARAM);
        }

        peWorkflowDAG.getNodes().forEach(node -> {
            Long nodeId = node.getNodeId();
            WorkflowDAG.Node n = new WorkflowDAG.Node(node);
            id2Node.put(nodeId, n);
            rootIds.add(nodeId);
        });

        peWorkflowDAG.getEdges().forEach(edge -> {
            WorkflowDAG.Node from = id2Node.get(edge.getFrom());
            WorkflowDAG.Node to = id2Node.get(edge.getTo());

            if (from == null || to == null) {
                throw new PlatformException(BaseErrorCode.PARAM);
            }

            from.getSuccessors().add(to);
            from.getSuccessorEdgeMap().put(to, edge);
            to.getDependencies().add(from);
            to.getDependenceEdgeMap().put(from, edge);
            rootIds.remove(to.getNodeId());
        });

        if (rootIds.isEmpty()) {
            throw new PlatformException(BaseErrorCode.PARAM);
        }

        List<WorkflowDAG.Node> roots = new LinkedList<>();
        rootIds.forEach(id -> roots.add(id2Node.get(id)));
        return new WorkflowDAG(roots, id2Node);
    }
}
