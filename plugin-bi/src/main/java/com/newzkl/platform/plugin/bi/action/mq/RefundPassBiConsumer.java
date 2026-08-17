package com.newzkl.platform.plugin.bi.action.mq;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.plugin.bi.domain.service.AdminEventDomain;
import com.newzkl.platform.plugin.bi.domain.service.impl.AdminEventDomainImpl;
import com.newzkl.platform.plugin.bi.model.event.BiRefundPassEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 退款通过消费者
 *
 * <p>消费 {@code REFUND_PASS}, 写 TodoDO(售后中+1)。</p>
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.REFUND_PASS_BI_MESSAGE, tag = MQ.Tag.REFUND_PASS)
public class RefundPassBiConsumer extends AbstractMessageMQPushConsumer<BiRefundPassEvent> {

    @Autowired
    private AdminEventDomain adminEventDomain;

    @Override
    public void remoteProcess(BiRefundPassEvent message, Map<String, Object> extMap) {
        log.info("BI 退款通过消费, orderId: {}, refundAmount: {}", message.getOrderId(), message.getRefundAmount());
        adminEventDomain.onRefundPass(CommonEnum.Client.ADMIN, null, message.getRefundAmount());
    }
}
