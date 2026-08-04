package com.newzkl.platform.plugin.audit.workflow.dag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 引用表示法 DAG
 *
 * <p>节点内记录上下游连接关系, 由点线表示法转换而来, 因含对象引用无法直接 JSON 序列化</p>
 *
 * @author KC
 */
@Data
@ToString(exclude = {"nodeMap"})
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowDAG {

    /** 顶点集合, 允许存在多个顶点 */
    private List<Node> roots;

    /** 节点主键到节点的映射 */
    private Map<Long, Node> nodeMap;

    /**
     * 按主键取节点
     *
     * @param nodeId 节点主键
     * @return 匹配节点, 未命中返回 null
     */
    public Node getNode(Long nodeId) {
        if (nodeMap == null) {
            return null;
        }
        return nodeMap.get(nodeId);
    }

    /**
     * 引用表示法节点
     */
    @Getter
    @Setter
    @EqualsAndHashCode(exclude = {"dependencies", "dependenceEdgeMap", "successorEdgeMap", "holder", "successors"})
    @ToString(exclude = {"dependencies", "dependenceEdgeMap", "successorEdgeMap", "holder"})
    @NoArgsConstructor
    public static final class Node {

        /** 节点主键 */
        private Long nodeId;

        /** 承载的点线表示法节点 */
        private PEWorkflowDAG.Node holder;

        /** 依赖的上游节点 */
        private List<Node> dependencies;

        /** 连接上游节点的边 */
        private Map<Node, PEWorkflowDAG.Edge> dependenceEdgeMap;

        /** 后继子节点 */
        private List<Node> successors;

        /** 连接后继节点的边 */
        private Map<Node, PEWorkflowDAG.Edge> successorEdgeMap;

        public Node(PEWorkflowDAG.Node node) {
            this.nodeId = node.getNodeId();
            this.holder = node;
            this.dependencies = new LinkedList<>();
            this.dependenceEdgeMap = new HashMap<>();
            this.successors = new LinkedList<>();
            this.successorEdgeMap = new HashMap<>();
        }
    }
}
