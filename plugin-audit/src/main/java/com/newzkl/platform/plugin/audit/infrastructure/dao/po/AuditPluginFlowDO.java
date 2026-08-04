package com.newzkl.platform.plugin.audit.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.AuditBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审批流
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("audit_flow")
public class AuditPluginFlowDO extends AuditBaseDO {

    /** 审批模板主键 */
    private Long templateId;

    /** 申请人账号主键 */
    private Long accountId;

    /** 申请人账号名称 */
    private String username;

    /** 申请人角色主键 */
    private Long roleId;

    /** 审批状态 0 待用户提交 1 待审核 2 通过 3 未通过 4 终止 */
    private Integer state;

    /** 当前节点 code */
    private String currentCode;

    /** 上下文参数 JSON */
    private String contextParams;

    /** 最后拒绝原因 */
    private String lastRefuseReason;

    /** 是否最新 1 是 0 否 */
    private Integer isNew;
}
