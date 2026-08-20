package com.newzkl.platform.plugin.bi.action.mq;

import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.plugin.bi.domain.service.AdminEventDomain;
import com.newzkl.platform.plugin.bi.model.event.BiGoodsPaySuccessEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 商品订单支付成功消费者
 *
 * <p>消费 {@code GOODS_ORDER_PAY_SUCCESS}(仅商品订单), 统计字段与商品购买有关的宽表:
 * TradeDO(交易额/订单量) + TodoDO(待付款-1/待发货+1) + 商品/店铺排行 + CorrelationDO。</p>
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.GOODS_ORDER_PAY_SUCCESS_BI_MESSAGE, tag = MQ.Tag.GOODS_ORDER_PAY_SUCCESS)
public class GoodsPaySuccessBiConsumer extends AbstractMessageMQPushConsumer<BiGoodsPaySuccessEvent> {

    @Autowired
    private AdminEventDomain adminEventDomain;

    @Override
    public void remoteProcess(BiGoodsPaySuccessEvent message, Map<String, Object> extMap) {
        log.info("BI 商品订单支付成功消费, orderId: {}", message.getOrderId());
        adminEventDomain.onGoodsPaySuccess(AccountEnum.Client.ADMIN,
                message.getUserId(), message.getStoreId(), message.getGoodsId(), message.getAmount());
    }
}
