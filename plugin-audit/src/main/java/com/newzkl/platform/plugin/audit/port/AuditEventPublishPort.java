package com.newzkl.platform.plugin.audit.port;

import com.newzkl.platform.plugin.audit.workflow.model.AuditEventMsg;

/**
 * 审批事件发布出站端口
 *
 * <p>审批到终态时发布事件, 经本地消息表投递, 实现落 adapter 层转调 core-mq LocalMessageDomain</p>
 *
 * @author KC
 */
public interface AuditEventPublishPort {

    /**
     * 发布审批事件
     *
     * @param tag 事件路由标签
     * @param msg 审批事件消息
     */
    void publish(String tag, AuditEventMsg msg);
}
