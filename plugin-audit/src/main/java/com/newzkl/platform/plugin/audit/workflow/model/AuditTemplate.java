package com.newzkl.platform.plugin.audit.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 审批模板
 *
 * <p>定义审批流程的节点与流转, node 与 edge 为点线表示法 JSON, 存 audit_template 表</p>
 *
 * @author KC
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditTemplate {

    /** 模板主键 */
    private Long id;

    /** 模板类型, 工厂按此分派策略 1 角色申请 2 保证金 3 SPU上传 4 品牌申请 5 SPU工单 6 实名认证 */
    private Integer templateType;

    /** 模板名称 */
    private String name;

    /** 审批节点信息 JSON */
    private String node;

    /** 审批节点流转信息 JSON */
    private String edge;
}
