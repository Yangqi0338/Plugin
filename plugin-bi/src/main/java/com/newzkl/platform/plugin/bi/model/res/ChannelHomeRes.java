package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 渠道商 HOME 总览
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

    /** 本周交易走势(销售额, 万元) */
    private List<WeekTradeItem> weekTradeList;

    /**
     * 周交易项
     */
    @Data
    public static class WeekTradeItem {

        /** 星期(周一) */
        private String weekDay;

        /** 销售额(万元) */
        private BigDecimal amount;
    }
}
