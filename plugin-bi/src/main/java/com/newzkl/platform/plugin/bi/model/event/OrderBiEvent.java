package com.newzkl.platform.plugin.bi.model.event;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单 BI 事件入参(支付/下单/售后)
 *
 * <p>由调用侧(MQ 或业务)组装投递。标记: 调用侧尚未接通, 后续补。</p>
 */
@Data
public class OrderBiEvent implements Serializable {

    /** 事件类型: ORDER_CREATE(下单) / ORDER_PAY(支付) / ORDER_REFUND(售后) */
    private String eventType;

    /** 订单ID */
    private Long orderId;

    /** 租户 */
    private Long clientId;

    /** 用户 */
    private Long userId;

    /** 门店 */
    private Long storeId;

    /** SPU */
    private Long spuId;

    /** 交易额(GMV, 元) — 支付事件必填 */
    private BigDecimal amount;

    /** 退款额(元) — 售后事件必填 */
    private BigDecimal refundAmount;
}
