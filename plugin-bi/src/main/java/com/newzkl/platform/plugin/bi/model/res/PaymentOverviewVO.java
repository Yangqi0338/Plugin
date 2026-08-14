package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付中心总览(平台身份)
 */
@Data
public class PaymentOverviewVO {

    /** 今日支付总额(元) */
    private BigDecimal todayPayAmount;

    /** 较昨日增幅(%) */
    private String momRatio;

    /** 今日手续费支出(元) */
    private BigDecimal todayFeeAmount;

    /** 综合费率(%) */
    private String feeRate;

    /** 分账结算(笔) */
    private Integer splitCount;

    /** 分账说明 */
    private String splitDesc;

    /** 对账差异(笔) */
    private Integer reconDiffCount;

    /** 连续零差异天数 */
    private Integer zeroDiffDays;
}
