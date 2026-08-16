package com.newzkl.platform.plugin.bi.action.mq;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.plugin.bi.domain.constant.BiTriggerType;
import com.newzkl.platform.plugin.bi.domain.service.BiEventWriteService;
import com.newzkl.platform.plugin.bi.model.event.BiTriggerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * BI 模拟触发消费者
 *
 * <p>消费 {@code bi:trigger} 模拟事件, 按 {@link BiTriggerType} 分发写宽表。
 * 生产环境各业务域发真实事件后废弃。</p>
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.BI_TRIGGER_EVENT_MESSAGE, tag = MQ.Tag.BI_TRIGGER_EVENT)
public class BiTriggerConsumer extends AbstractMessageMQPushConsumer<BiTriggerEvent> {

    @Autowired
    private BiEventWriteService biEventWriteService;

    @Override
    public void remoteProcess(BiTriggerEvent message, Map<String, Object> extMap) {
        log.info("BI 模拟事件消费, type: {}, bizId: {}", message.getTriggerType(), message.getBizId());
        BiTriggerType type = BiTriggerType.valueOf(message.getTriggerType());
        switch (type) {
            case MEMBER_REGISTER -> biEventWriteService.onMemberRegister(CommonEnum.Client.ADMIN, message.getMemberId(), message.getLevel());
            case EVIDENCE_ON_CHAIN -> biEventWriteService.onEvidenceOnChain(CommonEnum.Client.ADMIN, message.getEvidenceId(), message.getUserId());
            case INVENTORY_CHANGE -> biEventWriteService.onInventoryChange(CommonEnum.Client.ADMIN, message.getGoodsId(), message.getStoreId(), message.getAmount(), message.getLevel());
            case GOODS_ON_SHELF -> biEventWriteService.onGoodsStatus(CommonEnum.Client.ADMIN, "ON_SHELF");
            case GOODS_OFF_SHELF -> biEventWriteService.onGoodsStatus(CommonEnum.Client.ADMIN, "OFF_SHELF");
            case GOODS_AUDIT -> biEventWriteService.onGoodsStatus(CommonEnum.Client.ADMIN, "AUDIT");
            case ORDER_CREATE -> biEventWriteService.onOrderCreate(CommonEnum.Client.ADMIN, message.getUserId());
            case ORDER_PAY -> biEventWriteService.onOrderPay(CommonEnum.Client.ADMIN, message.getBizId());
            default -> log.warn("未支持模拟类型: {}", message.getTriggerType());
        }
    }
}
