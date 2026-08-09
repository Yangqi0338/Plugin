package com.newzkl.platform.plugin.audit.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.AuditBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审批日志
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName
public class AuditFlowLogDO extends AuditBaseDO {

    /** 审批单主键 */
    private Long flowId;

    /** 审批人账号主键 */
    private Long accountId;

    /** 审批人名称 */
    private String accountName;

    /** 审批动作 0 拒绝 1 通过 */
    private Integer auditOperate;
}
