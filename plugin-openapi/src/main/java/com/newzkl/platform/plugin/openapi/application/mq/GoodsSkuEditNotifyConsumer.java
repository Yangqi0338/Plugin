package com.newzkl.platform.plugin.openapi.application.mq;

import com.newzkl.platform.base.biz.goods.model.goods.event.SkuEditEvent;
import com.newzkl.platform.base.biz.market.domain.relation.GoodsRelationDomain;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * SKU 增删改价开发者回调
 *
 * <p>三个 tag 共用同一负载 收件人为订阅该 SPU 的渠道商</p>
 *
 * @author KC
 */
@MQConsumer(consumerGroup = MQ.Tag.OPENAPI_GOODS_SKU_EDIT_MESSAGE,
        tag = {MQ.Tag.GOODS_SKU_EDIT_EVENT, MQ.Tag.GOODS_SKU_DELETE_EVENT, MQ.Tag.GOODS_SKU_PRICE_EVENT})
public class GoodsSkuEditNotifyConsumer extends AbstractDeveloperNotifyConsumer<SkuEditEvent> {

    @Autowired
    private GoodsRelationDomain goodsRelationDomain;

    @Override
    protected List<Long> accountIdList(SkuEditEvent message) {
        return goodsRelationDomain.channelIdListBySpuId(message.getSpuId());
    }

    @Override
    protected Object body(SkuEditEvent message, Map<String, Object> extMap) {
        return message;
    }
}
