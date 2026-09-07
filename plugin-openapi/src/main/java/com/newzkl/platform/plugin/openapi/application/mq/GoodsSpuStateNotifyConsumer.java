package com.newzkl.platform.plugin.openapi.application.mq;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.goods.model.goods.event.GoodsSaleStateEvent;
import com.newzkl.platform.base.biz.market.domain.relation.GoodsRelationDomain;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 商品上下架开发者回调
 *
 * <p>收件人为订阅该 SPU 的渠道商 由 biz-market 反查</p>
 *
 * @author KC
 */
@MQConsumer(consumerGroup = MQ.Tag.OPENAPI_GOODS_SPU_STATE_MESSAGE, tag = {MQ.Tag.GOODS_SPU_STATE_EVENT})
public class GoodsSpuStateNotifyConsumer extends AbstractDeveloperNotifyConsumer<GoodsSaleStateEvent> {

    @Autowired
    private GoodsRelationDomain goodsRelationDomain;

    @Override
    protected List<Long> accountIdList(GoodsSaleStateEvent message) {
        if (CollUtil.isEmpty(message.getSpuIdList())) {
            return Collections.emptyList();
        }
        return goodsRelationDomain.channelIdListBySpuId(message.getSpuIdList().get(0));
    }

    @Override
    protected Object body(GoodsSaleStateEvent message, Map<String, Object> extMap) {
        return message;
    }
}
