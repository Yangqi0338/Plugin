package com.newzkl.platform.plugin.bi.application.service;

import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.plugin.bi.infrastructure.dao.DwsRealtimeOrderTrendDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.FactBlockchainDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.FactGoodsDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.FactInventoryDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.FactOrderDAO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.DwsRealtimeOrderTrend;
import com.newzkl.platform.plugin.bi.infrastructure.entity.FactBlockchain;
import com.newzkl.platform.plugin.bi.infrastructure.entity.FactGoods;
import com.newzkl.platform.plugin.bi.infrastructure.entity.FactInventory;
import com.newzkl.platform.plugin.bi.infrastructure.entity.FactOrder;
import com.newzkl.platform.plugin.bi.model.event.OrderBiEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * BI 事件消费编排服务
 *
 * <p>统一处理各类 BI 事件: 双写(事实表永久 + 实时宽表当日) + Redis 写时删缓存。
 * 补偿增量: 支付事件写 waitPayDelta=-1, waitDeliveryDelta=+1, 保证 SUM=存量。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BiEventService {

    /** 事件类型常量 */
    public static final String EVT_ORDER_CREATE = "ORDER_CREATE";
    public static final String EVT_ORDER_PAY = "ORDER_PAY";
    public static final String EVT_ORDER_REFUND = "ORDER_REFUND";
    public static final String EVT_INVENTORY_CHANGE = "INVENTORY_CHANGE";
    public static final String EVT_EVIDENCE = "EVIDENCE";

    private final FactOrderDAO factOrderDAO;
    private final FactInventoryDAO factInventoryDAO;
    private final FactGoodsDAO factGoodsDAO;
    private final FactBlockchainDAO factBlockchainDAO;
    private final DwsRealtimeOrderTrendDAO realtimeOrderTrendDAO;

    // ==================== 订单事件 ====================

    /**
     * 订单事件: 下单/支付/售后
     * 下单: 待付款+1; 支付: 待付款-1, 待发货+1, GMV+amount; 售后: 售后中+1, 退款+amount
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleOrderEvent(OrderBiEvent event) {
        // 1. 写事实表(永久)
        FactOrder fact = new FactOrder();
        fact.setOrderId(event.getOrderId());
        fact.setEventType(event.getEventType());
        fact.setClientId(event.getClientId());
        fact.setUserId(event.getUserId());
        fact.setStoreId(event.getStoreId());
        fact.setSpuId(event.getSpuId());
        fact.setAmount(event.getAmount() == null ? BigDecimal.ZERO : event.getAmount());
        fact.setEventTime(LocalDateTime.now());
        factOrderDAO.insert(fact);

        // 2. 写实时宽表(补偿增量)
        DwsRealtimeOrderTrend row = new DwsRealtimeOrderTrend();
        row.setUserId(event.getUserId());
        row.setClientId(event.getClientId());
        row.setStoreId(event.getStoreId());
        row.setEventType(event.getEventType());
        row.setWaitPayDelta(0);
        row.setWaitDeliveryDelta(0);
        row.setRefundingDelta(0);
        row.setOrderCount(0);
        row.setAmount(BigDecimal.ZERO);
        row.setRefundAmount(BigDecimal.ZERO);
        row.setOnChainCount(0);
        row.setEventTime(LocalDateTime.now());

        switch (event.getEventType()) {
            case EVT_ORDER_CREATE -> {
                row.setWaitPayDelta(1);
            }
            case EVT_ORDER_PAY -> {
                row.setWaitPayDelta(-1);       // 补偿: 待付款-1
                row.setWaitDeliveryDelta(1);   // 待发货+1
                row.setOrderCount(1);          // 今日订单+1
                row.setAmount(event.getAmount() == null ? BigDecimal.ZERO : event.getAmount()); // GMV
            }
            case EVT_ORDER_REFUND -> {
                row.setRefundingDelta(1);      // 售后中+1
                row.setRefundAmount(event.getRefundAmount() == null ? BigDecimal.ZERO : event.getRefundAmount());
            }
            default -> log.warn("未知订单事件类型: {}", event.getEventType());
        }
        realtimeOrderTrendDAO.insert(row);

        // 3. Redis 写时删缓存
        evictOverviewCache(event.getClientId());
    }

    // ==================== 库存事件 ====================

    /**
     * 库存变更: 写事实表(总库存/当前库存), 实时宽表只记变更(供库存紧张/售罄判断在查询时算)
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleInventoryEvent(Long skuId, Long clientId, Long storeId,
                                     String changeType, Integer totalStock, Integer currentStock) {
        FactInventory fact = new FactInventory();
        fact.setSkuId(skuId);
        fact.setClientId(clientId);
        fact.setStoreId(storeId);
        fact.setChangeType(changeType);
        fact.setTotalStock(totalStock);
        fact.setCurrentStock(currentStock);
        fact.setEventTime(LocalDateTime.now());
        factInventoryDAO.insert(fact);

        evictTodoCache(clientId);
    }

    // ==================== 商品事件 ====================

    /**
     * 商品上架/审核: 写事实表(供待审核商品数)
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleGoodsEvent(Long spuId, Long clientId, Long storeId, String eventType, String status) {
        FactGoods fact = new FactGoods();
        fact.setSpuId(spuId);
        fact.setClientId(clientId);
        fact.setStoreId(storeId);
        fact.setEventType(eventType);
        fact.setStatus(status);
        fact.setEventTime(LocalDateTime.now());
        factGoodsDAO.insert(fact);

        evictTodoCache(clientId);
    }

    // ==================== 知链事件(模拟) ====================

    /**
     * 上链/待确认: 写事实表 + 实时宽表(上链数+1)
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleEvidenceEvent(Long evidenceId, Long clientId, Long userId, String bizType, String certNo, String status) {
        FactBlockchain fact = new FactBlockchain();
        fact.setEvidenceId(evidenceId);
        fact.setEventType(EVT_EVIDENCE);
        fact.setClientId(clientId);
        fact.setUserId(userId);
        fact.setBizType(bizType);
        fact.setCertNo(certNo);
        fact.setStatus(status);
        fact.setEventTime(LocalDateTime.now());
        factBlockchainDAO.insert(fact);

        DwsRealtimeOrderTrend row = new DwsRealtimeOrderTrend();
        row.setUserId(userId);
        row.setClientId(clientId);
        row.setEventType(EVT_EVIDENCE);
        row.setWaitPayDelta(0);
        row.setWaitDeliveryDelta(0);
        row.setRefundingDelta(0);
        row.setOrderCount(0);
        row.setAmount(BigDecimal.ZERO);
        row.setRefundAmount(BigDecimal.ZERO);
        row.setOnChainCount(1); // 上链数+1
        row.setEventTime(LocalDateTime.now());
        realtimeOrderTrendDAO.insert(row);

        evictOverviewCache(clientId);
    }

    // ==================== Redis 缓存(写时删) ====================

    private void evictOverviewCache(Long clientId) {
        try {
            String key = "bi:overview:" + (clientId == null ? "global" : clientId);
            RedisUtil.del(key);
        } catch (Exception e) {
            log.warn("删除 overview 缓存失败 clientId={}", clientId, e);
        }
    }

    private void evictTodoCache(Long clientId) {
        try {
            String key = "bi:todo:" + (clientId == null ? "global" : clientId);
            RedisUtil.del(key);
        } catch (Exception e) {
            log.warn("删除 todo 缓存失败 clientId={}", clientId, e);
        }
    }
}
