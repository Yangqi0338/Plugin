package com.newzkl.platform.plugin.audit.workflow.dag;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 点线表示法 DAG
 *
 * <p>以节点集合与边集合描述审批流程图, 便于 JSON 序列化存储于 audit_template 的 node/edge 列</p>
 *
 * @author KC
 */
@Data
@NoArgsConstructor
public class PEWorkflowDAG implements Serializable {

    /** 流程图节点集合 */
    private List<Node> nodes;

    /** 流程图边集合 */
    private List<Edge> edges;

    /**
     * 节点
     */
    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    public static class Node implements Serializable {

        /** 节点主键 */
        private Long nodeId;

        /** 节点类型 0 审批节点 1 判断节点 */
        private Integer nodeType;

        /** 节点名称 */
        private String nodeName;

        /** 判断节点的条件表达式 */
        private String nodeParams;
    }

    /**
     * 边
     */
    @Data
    @NoArgsConstructor
    public static class Edge implements Serializable {

        /** 起始节点主键 */
        private Long from;

        /** 目标节点主键 */
        private Long to;

        /** 边属性, 判断节点分支取 true 或 false */
        private String property;

        /** 是否启用 */
        private Boolean enable;
    }

    public PEWorkflowDAG(List<Node> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges == null ? new LinkedList<>() : edges;
    }

    /**
     * 按节点名称查节点
     *
     * @param name 节点名称
     * @return 匹配节点, 未命中返回 null
     */
    public Node getNodeByName(String name) {
        for (Node node : nodes) {
            if (node.nodeName.trim().equals(name.trim())) {
                return node;
            }
        }
        return null;
    }

    /**
     * 按节点主键查节点
     *
     * @param id 节点主键
     * @return 匹配节点, 未命中返回 null
     */
    public Node getNodeById(Long id) {
        for (Node node : nodes) {
            if (node.nodeId.equals(id)) {
                return node;
            }
        }
        return null;
    }
}
