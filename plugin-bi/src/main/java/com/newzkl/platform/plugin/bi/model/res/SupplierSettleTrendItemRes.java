package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 供应商供货结算趋势项
 *
 * <p>柱状图数据点: 月份(x 轴) + 结算金额(万元, y 轴)。</p>
 */
@Data
public class SupplierSettleTrendItemRes implements Serializable {

    /** 月份(3月) */
    private String month;

    /** 结算金额(万元) */
    private BigDecimal amount;
}
