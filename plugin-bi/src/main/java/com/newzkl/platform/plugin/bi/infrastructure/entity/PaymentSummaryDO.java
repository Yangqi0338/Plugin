package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 支付中心·总览宽表
 *
 * <p>字段 = 支付中心查询展示字段。
 * 实时表: dws_realtime_admin_payment_summary | 日表: dws_day_admin_payment_summary</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN, suffix = "payment_summary")
public class PaymentSummaryDO extends BIBaseDO {

    /** 今日支付总额(元) */
    private BigDecimal todayPayAmount;

    /** 今日手续费支出(元) */
    private BigDecimal todayFeeAmount;

    /** 分账结算(笔) */
    private Integer splitCount;

    /** 对账差异(笔) */
    private Integer reconDiffCount;

    /** 连续零差异天数 */
    private Integer zeroDiffDays;
}
