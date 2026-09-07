package com.newzkl.platform.plugin.openapi.application.mq;

import com.newzkl.platform.base.biz.order.model.event.OrderStateEvent;
import com.newzkl.platform.base.biz.order.model.support.api.openapi.ApiOrderStateEvent;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 订单状态变更开发者回调
 *
 * <p>只关心渠道商订单 收件人即下单主体本身</p>
 *
 * @author KC
 */
@MQConsumer(consumerGroup = MQ.Tag.OPENAPI_ORDER_STATE_MESSAGE, tag = {MQ.Tag.ORDER_STATE_EVENT})
public class OrderStateNotifyConsumer extends AbstractDeveloperNotifyConsumer<OrderStateEvent> {

    @Override
    protected boolean accept(OrderStateEvent message) {
        return OrderEnum.OrderType.CHANNEL == message.getOrderType();
    }

    @Override
    protected List<Long> accountIdList(OrderStateEvent message) {
        return message.getChannelId() == null ? Collections.emptyList() : Collections.singletonList(message.getChannelId());
    }

    @Override
    protected Object body(OrderStateEvent message, Map<String, Object> extMap) {
        return new ApiOrderStateEvent(message.getOutOrderNo(), code(message.getSourceState()), code(message.getNewState()));
    }

    /**
     * 枚举状态转对外契约的 Integer
     *
     * @param state 状态枚举
     * @return 状态码 入参为 null 时返回 null
     */
    private static Integer code(OrderEnum.State state) {
        return state == null ? null : state.getCode();
    }
}
