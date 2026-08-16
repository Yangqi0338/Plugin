package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 渠道商 HOME 总览
 *
 * <p>本周交易走势见独立接口 {@code /bi/channel/weekTrade} → List&lt;ChannelWeekTradeItemRes&gt;。</p>
 */
@Data
public class ChannelHomeRes implements Serializable {

    /** 本月总订单金额(元) */
    private BigDecimal monthOrderAmount;

    /** 本月总订单金额环比(%) */
    private BigDecimal orderAmountMomRatio;

    /** 本月总订单数 */
    private Integer monthOrderCount;

    /** 今日订单数 */
    private Integer todayOrderCount;

    /** 采购金余额(元) */
    private BigDecimal purchaseBalance;

    /** 商品席位总数 */
    private Integer seatTotal;

    /** 已用商品席位 */
    private Integer seatUsed;
}
