package com.newzkl.platform.plugin.audit.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 审批事件消息
 *
 * <p>审批流转到终态时外发的事件体, 由 AuditFlow 字段拷贝加业务数据 JSON 与 tag 组成, 经本地消息表投递</p>
 *
 * @author KC
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEventMsg {

    /** 审批流主键 */
    private Long id;

    /** 申请人账号主键 */
    private Long accountId;

    /** 申请人账号名称 */
    private String username;

    /** 审批模板主键 */
    private Long templateId;

    /** 审批状态 1 审批中 2 通过 3 拒绝 4 终止 */
    private Integer state;

    /** 事件路由标签, 决定消息投递到哪个消费方 */
    private String tag;

    /** 业务数据 JSON, 由策略 getDataVO 序列化 */
    private String data;
}
