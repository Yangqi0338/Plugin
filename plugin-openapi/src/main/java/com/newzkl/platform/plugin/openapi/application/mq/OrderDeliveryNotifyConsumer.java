package com.newzkl.platform.plugin.openapi.application.mq;

import com.newzkl.platform.base.biz.order.model.event.OrderDeliveryEvent;
import com.newzkl.platform.base.biz.order.model.support.api.openapi.ApiDeliverEvent;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 订单发货开发者回调
 *
 * <p>只关心渠道商订单 收件人即下单主体本身</p>
 *
 * @author KC
 */
@MQConsumer(consumerGroup = MQ.Tag.OPENAPI_ORDER_DELIVERY_MESSAGE, tag = {MQ.Tag.ORDER_DELIVERY_EVENT})
public class OrderDeliveryNotifyConsumer extends AbstractDeveloperNotifyConsumer<OrderDeliveryEvent> {

    @Override
    protected boolean accept(OrderDeliveryEvent message) {
        return OrderEnum.OrderType.CHANNEL == message.getOrderType();
    }

    @Override
    protected List<Long> accountIdList(OrderDeliveryEvent message) {
        return message.getChannelId() == null ? Collections.emptyList() : Collections.singletonList(message.getChannelId());
    }

    @Override
    protected Object body(OrderDeliveryEvent message, Map<String, Object> extMap) {
        return new ApiDeliverEvent(message.getOutOrderNo(), message.getSkuDeliverList(), message.getExpressName(), message.getExpressNo());
    }
}
