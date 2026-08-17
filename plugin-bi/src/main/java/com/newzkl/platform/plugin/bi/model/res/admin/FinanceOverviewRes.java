package com.newzkl.platform.plugin.bi.model.res.admin;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 财务结算总览
 */
@Data
public class FinanceOverviewRes implements Serializable {

    /** 本月GMV(元) */
    private BigDecimal monthGmv;

    /** 待结算金额(元) */
    private BigDecimal pendingSettleAmount;

    /** 本月退款(元) */
    private BigDecimal monthRefundAmount;

    /** 分销佣金支出(元) */
    private BigDecimal commissionCost;
}
