package com.newzkl.platform.plugin.bi.action.mq;

import com.newzkl.platform.base.biz.order.model.support.api.order.RefundPassEvent;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.plugin.bi.application.service.BiEventService;
import com.newzkl.platform.plugin.bi.model.event.OrderBiEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 订单售后 BI 消费者(订阅 REFUND_PASS)
 *
 * <p>售后通过 → 写事实表 + 实时宽表(售后中+1, 退款额累加)。</p>
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.REFUND_PASS_MESSAGE, tag = MQ.Tag.REFUND_PASS)
@RequiredArgsConstructor
public class RefundPassBiConsumer extends AbstractMessageMQPushConsumer<RefundPassEvent> {

    private final BiEventService biEventService;

    @Override
    public void remoteProcess(RefundPassEvent message, Map<String, Object> extMap) {
        log.info("[BI] 售后通过事件: orderId={}, refundAmount={}", message.getOrderId(), message.getRefundAmount());

        OrderBiEvent event = new OrderBiEvent();
        event.setEventType(BiEventService.EVT_ORDER_REFUND);
        event.setOrderId(message.getSpuOrderId() != null ? message.getSpuOrderId() : message.getOrderId());
        event.setUserId(message.getChannelId()); // 缺省: 用户ID暂用渠道商ID, 调用侧后续修正
        event.setClientId(null);
        event.setRefundAmount(message.getRefundAmount() == null ? BigDecimal.ZERO : BigDecimal.valueOf(message.getRefundAmount()));
        biEventService.handleOrderEvent(event);
    }
}
