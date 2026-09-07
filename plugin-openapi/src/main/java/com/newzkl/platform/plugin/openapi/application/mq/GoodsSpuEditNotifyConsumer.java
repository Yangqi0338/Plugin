package com.newzkl.platform.plugin.openapi.application.mq;

import com.newzkl.platform.base.biz.goods.model.goods.event.SpuEditEvent;
import com.newzkl.platform.base.biz.market.domain.relation.GoodsRelationDomain;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * SPU 编辑开发者回调
 *
 * <p>收件人为订阅该 SPU 的渠道商 由 biz-market 反查</p>
 *
 * @author KC
 */
@MQConsumer(consumerGroup = MQ.Tag.OPENAPI_GOODS_SPU_EDIT_MESSAGE, tag = {MQ.Tag.GOODS_SPU_EDIT_EVENT})
public class GoodsSpuEditNotifyConsumer extends AbstractDeveloperNotifyConsumer<SpuEditEvent> {

    @Autowired
    private GoodsRelationDomain goodsRelationDomain;

    @Override
    protected List<Long> accountIdList(SpuEditEvent message) {
        return goodsRelationDomain.channelIdListBySpuId(message.getSpuId());
    }

    @Override
    protected Object body(SpuEditEvent message, Map<String, Object> extMap) {
        return message;
    }
}
