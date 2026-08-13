package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 首页工作台 - 实时概况
 *
 * <p>对应用户首页「实时概况」卡片: 今日核心经营指标 + 待办角标。</p>
 * <p>金额单位: 元(BigDecimal); 数量: Integer。</p>
 */
@Data
public class HomeOverviewVO implements Serializable {

    /** 今日 GMV(元) */
    private BigDecimal todayGmv;

    /** 今日订单数 */
    private Integer todayOrderCount;

    /** 今日支付订单数 */
    private Integer todayPayOrderCount;

    /** 今日新增会员 */
    private Integer todayMemberCount;

    /** 今日存证数 */
    private Integer todayEvidenceCount;

    /** 今日核验次数 */
    private Integer todayVerifyCount;

    /** 本月 GMV(元) */
    private BigDecimal monthGmv;

    /** 本月订单数 */
    private Integer monthOrderCount;

    /** 库存预警数 */
    private Integer stockWarnCount;

    /** 待付款订单数(角标) */
    private Integer waitPayCount;

    /** 待发货订单数(角标) */
    private Integer waitDeliveryCount;

    /** 售后中订单数(角标) */
    private Integer refundingCount;
}
