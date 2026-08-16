package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付中心总览
 * @ext 沿用实时表逻辑, 字段无 today 前缀
 */
@Data
public class PaymentOverviewRes implements Serializable {

    /** 支付总额(元) */
    private BigDecimal payAmount;

    /** 手续费支出(元) */
    private BigDecimal feeAmount;

    /** 分账结算(笔) */
    private Integer splitCount;

    /** 对账差异(笔) */
    private Integer reconDiffCount;

    /** 较昨日提升比(%) */
    private BigDecimal momRatio;

    /** 综合费率(%)
     * @ext 手续费/支付额
     */
    private BigDecimal feeRate;
}
