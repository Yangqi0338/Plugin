package com.newzkl.platform.plugin.bi.action.mq;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.plugin.bi.domain.service.BiEventWriteService;
import com.newzkl.platform.plugin.bi.model.event.BiPaySuccessEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 支付成功消费者(任何支付)
 *
 * <p>消费 {@code PAYMENT_PAY_SUCCESS}, 写 OverviewDO(今日交易额/支付订单) + PaymentSummaryDO(支付总额)。
 * 与商品订单支付({@code GOODS_ORDER_PAY_SUCCESS})区分: 本消费者统计与商品购买无关的全量支付。</p>
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.PAYMENT_PAY_SUCCESS_MESSAGE, tag = MQ.Tag.PAYMENT_PAY_SUCCESS)
public class PaySuccessBiConsumer extends AbstractMessageMQPushConsumer<BiPaySuccessEvent> {

    @Autowired
    private BiEventWriteService biEventWriteService;

    @Override
    public void remoteProcess(BiPaySuccessEvent message, Map<String, Object> extMap) {
        log.info("BI 支付成功消费, orderId: {}", message.getOrderId());
        // TODO: 金额需回查订单(事件仅含 orderId), 当前传 null 占位, 待订单查询端口接入
        biEventWriteService.onPaySuccess(CommonEnum.Client.ADMIN, null, null);
    }
}
