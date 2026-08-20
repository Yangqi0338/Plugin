package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 财务结算·本月汇总宽表
 *
 * <p>字段 = 财务结算查询展示字段。
 * 实时表: dws_realtime_admin_finance_summary | 日表: dws_day_admin_finance_summary</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = AccountEnum.Client.ADMIN)
public class FinanceSummaryDO extends BIBaseDO {

    /** 本月GMV(元)
     */
    private BigDecimal monthGmv;

    /** 待结算金额(元)
     */
    private BigDecimal pendingSettleAmount;

    /** 本月退款(元)
     */
    private BigDecimal monthRefundAmount;

    /** 分销佣金支出(元)
     */
    private BigDecimal commissionCost;
}
