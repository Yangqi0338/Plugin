package com.newzkl.platform.plugin.bi.model.res.supplier;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 供应商 HOME 总览
 *
 * <p>供货结算趋势(万元)见独立接口 {@code /bi/supplier/settleTrend} → List&lt;SupplierSettleTrendItemRes&gt;。</p>
 */
@Data
public class HomeOverviewRes implements Serializable {

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
}
