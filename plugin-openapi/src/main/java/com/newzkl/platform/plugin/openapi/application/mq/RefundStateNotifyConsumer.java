package com.newzkl.platform.plugin.openapi.application.mq;

import com.newzkl.platform.base.biz.order.model.event.RefundStateEvent;
import com.newzkl.platform.base.biz.order.model.support.api.openapi.ApiRefundStateEvent;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 售后单状态变更开发者回调
 *
 * <p>只关心渠道商订单 收件人即下单主体本身</p>
 *
 * @author KC
 */
@MQConsumer(consumerGroup = MQ.Tag.OPENAPI_REFUND_STATE_MESSAGE, tag = {MQ.Tag.REFUND_STATE_EVENT})
public class RefundStateNotifyConsumer extends AbstractDeveloperNotifyConsumer<RefundStateEvent> {

    @Override
    protected boolean accept(RefundStateEvent message) {
        return OrderEnum.OrderType.CHANNEL == message.getOrderType();
    }

    @Override
    protected List<Long> accountIdList(RefundStateEvent message) {
        return message.getChannelId() == null ? Collections.emptyList() : Collections.singletonList(message.getChannelId());
    }

    @Override
    protected Object body(RefundStateEvent message, Map<String, Object> extMap) {
        return new ApiRefundStateEvent(message.getRefundId(), code(message.getSourceState()), code(message.getNewState()));
    }

    /**
     * 枚举状态转对外契约的 Integer
     *
     * @param state 状态枚举
     * @return 状态码 入参为 null 时返回 null
     */
    private static Integer code(RefundEnum.State state) {
        return state == null ? null : state.getCode();
    }
}
