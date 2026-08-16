package com.newzkl.platform.plugin.bi.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退款通过事件
 * @ext REFUND_PASS 触发, 含退款金额与商品维度
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BiRefundPassEvent implements Serializable {

    /** 售后单ID */
    private Long id;

    /** 订单ID */
    private Long orderId;

    /** 商品ID */
    private Long goodsId;

    /** 退款金额(元) */
    private BigDecimal refundAmount;
}
