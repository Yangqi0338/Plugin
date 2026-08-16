package com.newzkl.platform.plugin.bi.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 支付成功事件
 * @ext 任何支付渠道触发(PAYMENT_PAY_SUCCESS), 仅含订单ID
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BiPaySuccessEvent implements Serializable {

    /** 交易ID */
    private Long orderId;
}
