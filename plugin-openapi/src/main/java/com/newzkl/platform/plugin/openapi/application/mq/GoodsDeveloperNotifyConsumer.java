package com.newzkl.platform.plugin.openapi.application.mq;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.market.domain.relation.GoodsRelationDomain;
import com.newzkl.platform.base.biz.market.model.dto.relation.GoodsRelationQueryDTO;
import com.newzkl.platform.base.biz.market.model.dto.relation.MarketGoodsRelationDTO;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.utils.NotifyUtil;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.core.mq.model.notify.NotifyEnums;
import com.newzkl.platform.base.common.core.mq.model.notify.NotifyEventCommand;
import com.newzkl.platform.base.common.ddd.model.enums.goods.GoodsRelationEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品开发者通知消费者(收件人解析)
 *
 * <p>消费 {@link MQ.Tag#GOODS_DEVELOPER_NOTIFY_EVENT} 事件。goods 域只发出 spuId + 业务类型 +
 * 事件内容(不含收件人); 本消费方经 biz-market {@link GoodsRelationDomain} 反查订阅该 SPU 的渠道账户,
 * 拿到 accountId 列表后经 {@link NotifyUtil} 转投既有 {@link MQ.Tag#DEVELOPER_NOTIFY_EVENT},
 * 由 {@link DeveloperNotifyConsumer} 完成开发者 HTTP 回调。</p>
 *
 * <p>收件人解析集中在 openapi 侧(唯一同时依赖 biz-goods 与 biz-market 的模块), goods 域零改动即可扩订阅关系。
 * 无订阅渠道时静默跳过, 不产生回调。</p>
 *
 * @author KC
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.GOODS_DEVELOPER_NOTIFY_EVENT_MESSAGE, tag = {MQ.Tag.GOODS_DEVELOPER_NOTIFY_EVENT})
public class GoodsDeveloperNotifyConsumer extends AbstractMessageMQPushConsumer<com.newzkl.platform.base.biz.goods.model.goods.event.GoodsDeveloperNotifyMq> {

    @Autowired
    private GoodsRelationDomain goodsRelationDomain;

    @Override
    public void remoteProcess(com.newzkl.platform.base.biz.goods.model.goods.event.GoodsDeveloperNotifyMq mq, Map<String, Object> extMap) {
        if (mq == null || mq.getSpuId() == null) {
            return;
        }
        List<Long> accountIds = resolveSubscriberAccountIds(mq.getSpuId());
        if (CollUtil.isEmpty(accountIds)) {
            log.info("商品开发者通知无订阅渠道 跳过 spuId={} businessType={}", mq.getSpuId(), mq.getBusinessType());
            return;
        }
        NotifyEventCommand command = new NotifyEventCommand();
        command.setServiceType(NotifyEnums.ServiceType.GOODS.getCode());
        command.setBusinessType(mq.getBusinessType());
        command.setEventInfo(mq.getEventContent());
        NotifyUtil.batchSend(accountIds, command);
        log.info("商品开发者通知已转投 spuId={} businessType={} 收件人数={}", mq.getSpuId(), mq.getBusinessType(), accountIds.size());
    }

    /**
     * 反查订阅指定 SPU 的渠道账户ID列表
     *
     * <p>按 goodsId=spuId + relationType=SELECT_GOODS(市场选品) 查商品关系, 取去重 userId 列表。
     * userId 即渠道商账户ID(&gt;0 为客户), 是开发者回调的收件人。</p>
     *
     * @param spuId SPU 主键
     * @return 订阅渠道账户ID列表, 无则空列表
     */
    private List<Long> resolveSubscriberAccountIds(Long spuId) {
        GoodsRelationQueryDTO query = new GoodsRelationQueryDTO();
        query.setGoodsId(spuId);
        query.setRelationType(GoodsRelationEnum.GoodsRelation.SELECT_GOODS);
        List<MarketGoodsRelationDTO> relationList = goodsRelationDomain.queryGoodsRelationListByDTO(query);
        if (CollUtil.isEmpty(relationList)) {
            return List.of();
        }
        return relationList.stream()
                .map(MarketGoodsRelationDTO::getUserId)
                .filter(userId -> userId != null && userId > 0)
                .distinct()
                .collect(Collectors.toList());
    }
}
