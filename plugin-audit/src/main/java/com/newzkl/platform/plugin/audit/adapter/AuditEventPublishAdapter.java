package com.newzkl.platform.plugin.audit.adapter;

import com.alibaba.fastjson2.JSON;
import com.newzkl.platform.base.common.core.mq.domain.LocalMessageDomain;
import com.newzkl.platform.base.common.core.mq.model.dto.LocalMessageDTO;
import com.newzkl.platform.plugin.audit.port.AuditEventPublishPort;
import com.newzkl.platform.plugin.audit.workflow.model.AuditEventMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 审批事件发布适配器
 *
 * <p>审批到终态时经 core-mq 本地消息表投递事件: 组 LocalMessageDTO 落库, 由发送 Job 异步投递, 替代 Seata/直发 RocketMQ</p>
 *
 * @author KC
 */
@Component("auditPluginAuditEventPublishAdapter")
@RequiredArgsConstructor
public class AuditEventPublishAdapter implements AuditEventPublishPort {

    /**
     * 审批事件投递 topic
     */
    private static final String AUDIT_EVENT_TOPIC = "AUDIT_EVENT";

    private final LocalMessageDomain localMessageDomain;

    @Override
    public void publish(String tag, AuditEventMsg msg) {
        LocalMessageDTO dto = new LocalMessageDTO();
        dto.setTopic(AUDIT_EVENT_TOPIC);
        dto.setTag(tag);
        dto.setOutKey(msg.getId() == null ? null : String.valueOf(msg.getId()));
        dto.setMessageContent(JSON.toJSONString(msg));
        dto.setMessageClass(AuditEventMsg.class.getName());
        localMessageDomain.create(dto);
    }
}
