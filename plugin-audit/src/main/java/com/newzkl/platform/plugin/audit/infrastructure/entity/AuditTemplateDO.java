package com.newzkl.platform.plugin.audit.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.AuditBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审批模板
 *
 * @author KC
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName
public class AuditTemplateDO extends AuditBaseDO {

    /** 模板类型 1 角色申请 2 保证金 3 SPU上传 4 品牌申请 5 SPU工单 6 实名认证 */
    private Integer templateType;

    /** 模板名称 */
    private String name;

    /** 审批节点信息 JSON */
    private String node;

    /** 审批节点流转信息 JSON */
    private String edge;
}
