package com.newzkl.platform.plugin.bi.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品订单支付成功事件
 * @ext 仅商品订单触发(GOODS_ORDER_PAY_SUCCESS), 含商品维度明细
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BiGoodsPaySuccessEvent implements Serializable {

    /** 订单ID */
    private Long orderId;

    /** 用户ID */
    private Long userId;

    /** 门店ID */
    private Long storeId;

    /** 商品ID */
    private Long goodsId;

    /** 支付金额(元) */
    private BigDecimal amount;
}
