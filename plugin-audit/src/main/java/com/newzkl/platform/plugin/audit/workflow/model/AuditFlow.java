package com.newzkl.platform.plugin.audit.workflow.model;

import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 审批流
 *
 * <p>一次审批请求的流转载体, 记录当前节点与状态, 存 audit_flow 表</p>
 *
 * @author KC
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditFlow {

    /** 审批单主键 */
    private Long id;

    /** 申请人账号主键 */
    private Long accountId;

    /** 申请人角色主键 */
    private RoleEnum.CompanyRole role;

    /** 申请人账号名称 */
    private String username;

    /** 审批模板主键 */
    private Long templateId;

    /** 审批状态 0 待用户提交 1 待审核 2 通过 3 未通过 4 终止 */
    private Integer state;

    /** 最后拒绝原因 */
    private String lastRefuseReason;

    /** 当前节点 code */
    private String currentCode;

    /** 上下文参数 JSON */
    private String contextParams;

    /** 是否最新 1 是 0 否 */
    private Integer isNew;

    /** 当前审批权限信息 JSON */
    private String currentAuditPermissionVO;

    /** 创建时间 */
    private LocalDateTime createTime;
}
