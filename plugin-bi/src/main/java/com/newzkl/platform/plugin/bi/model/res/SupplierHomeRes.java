package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 供应商 HOME 总览
 */
@Data
public class SupplierHomeRes implements Serializable {

    /** 在架供货SKU */
    private Integer onShelfSkuCount;

    /** 待审核(个) */
    private Integer pendingAuditCount;

    /** 已分发市场 */
    private Integer distributedMarketCount;

    /** 覆盖渠道(家) */
    private Integer coveredChannelCount;

    /** 本月供货订单(单) */
    private Integer monthSupplyOrderCount;

    /** 本月供货订单环比(%) */
    private BigDecimal orderMomRatio;

    /** 本月结算金额(元) */
    private BigDecimal monthSettleAmount;

    /** 回款及时率(%) */
    private BigDecimal settlementRate;

    /** 供货结算趋势(万元) */
    private List<SettleTrendItem> settleTrendList;

    /**
     * 结算趋势项
     */
    @Data
    public static class SettleTrendItem {

        /** 月份(3月) */
        private String month;

        /** 结算金额(万元) */
        private BigDecimal amount;
    }
}
