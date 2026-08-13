package com.newzkl.platform.plugin.bi.action.mock;

import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.bi.application.service.BiEventService;
import com.newzkl.platform.plugin.bi.model.event.OrderBiEvent;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * BI 事件 Mock 触发控制器
 *
 * <p>用于验证事件消费链路(下单/支付/库存/商品/上链)。
 * 标记: 下单/库存/上链的 MQ tag 未定义, 调用侧后续接通后移除本控制器。</p>
 */
@RestController("biMockController")
@RequestMapping("/bi/mock")
@RequiredArgsConstructor
public class BiMockController {

    private final BiEventService biEventService;

    /**
     * Mock 下单事件: 待付款+1
     */
    @PostMapping("/orderCreate")
    public PlatformResult<String> orderCreate(@RequestBody OrderBiEvent event) {
        event.setEventType(BiEventService.EVT_ORDER_CREATE);
        biEventService.handleOrderEvent(event);
        return PlatformResult.success("ok");
    }

    /**
     * Mock 支付事件: 待付款-1, 待发货+1, GMV+amount
     */
    @PostMapping("/orderPay")
    public PlatformResult<String> orderPay(@RequestBody OrderBiEvent event) {
        event.setEventType(BiEventService.EVT_ORDER_PAY);
        biEventService.handleOrderEvent(event);
        return PlatformResult.success("ok");
    }

    /**
     * Mock 库存变更: 总库存/当前库存
     */
    @PostMapping("/inventoryChange")
    public PlatformResult<String> inventoryChange(@RequestBody InventoryMockReq req) {
        biEventService.handleInventoryEvent(req.getSkuId(), req.getClientId(), req.getStoreId(),
                req.getChangeType(), req.getTotalStock(), req.getCurrentStock());
        return PlatformResult.success("ok");
    }

    /**
     * Mock 商品审核: 待审核商品
     */
    @PostMapping("/goodsAudit")
    public PlatformResult<String> goodsAudit(@RequestBody GoodsMockReq req) {
        biEventService.handleGoodsEvent(req.getSpuId(), req.getClientId(), req.getStoreId(),
                req.getEventType(), req.getStatus());
        return PlatformResult.success("ok");
    }

    /**
     * Mock 上链事件: 上链数+1
     */
    @PostMapping("/evidence")
    public PlatformResult<String> evidence(@RequestBody EvidenceMockReq req) {
        biEventService.handleEvidenceEvent(req.getEvidenceId(), req.getClientId(), req.getUserId(),
                req.getBizType(), req.getCertNo(), req.getStatus());
        return PlatformResult.success("ok");
    }

    // ==================== Mock 入参 ====================

    @Data
    public static class InventoryMockReq implements Serializable {
        public Long skuId;
        public Long clientId;
        public Long storeId;
        public String changeType;
        public Integer totalStock;
        public Integer currentStock;
    }

    @Data
    public static class GoodsMockReq implements Serializable {
        public Long spuId;
        public Long clientId;
        public Long storeId;
        public String eventType;
        public String status;
    }

    @Data
    public static class EvidenceMockReq implements Serializable {
        public Long evidenceId;
        public Long clientId;
        public Long userId;
        public String bizType;
        public String certNo;
        public String status;
    }
}
