package com.newzkl.platform.plugin.audit.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 审批日志
 *
 * <p>逐次审批动作留痕, 存 audit_flow_log 表</p>
 *
 * @author KC
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditFlowLog {

    /** 日志主键 */
    private Long id;

    /** 审批单主键 */
    private Long flowId;

    /** 审批人账号主键 */
    private Long accountId;

    /** 审批人名称 */
    private String accountName;

    /** 审批动作 0 拒绝 1 通过 */
    private Integer auditOperate;
}
