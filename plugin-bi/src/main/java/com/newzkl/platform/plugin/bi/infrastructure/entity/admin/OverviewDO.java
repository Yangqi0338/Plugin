package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 首页·实时概况宽表(一个查询对应一个宽表)
 *
 * <p>字段 = 实时概况查询展示字段, 事件落库时累加。
 * 实时表: dws_realtime_admin_overview_trend(今日, 无 bizDate)
 * 日表:   dws_day_admin_overview_trend(T+1, 含 bizDate, 供昨日对比/本月累计)</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = AccountEnum.Client.ADMIN)
public class OverviewDO extends BIBaseDO {

    /** 交易额(元)
     * @ext 订单支付时 +amount
     */
    private BigDecimal gmv;

    /** 支付订单(笔)
     * @ext 订单支付时 +1
     */
    private Integer payOrderCount;

    /** 新增会员(人)
     * @ext 会员注册时 +1
     */
    private Integer memberCount;

    /** 上链存证(条)
     * @ext 上链事件时 +1
     */
    private Integer evidenceCount;
    
}
