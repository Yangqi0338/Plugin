package com.newzkl.platform.plugin.bi.model.res.partner;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 服务商 HOME 总览
 */
@Data
public class HomeOverviewRes implements Serializable {

    /** 在架资源服务(项) */
    private Integer onShelfServiceCount;

    /** 本月新增(项) */
    private Integer monthNewCount;

    /** 累计被调用(次) */
    private Integer totalCallCount;

    /** 覆盖渠道(家) */
    private Integer coveredChannelCount;

    /** 本月分润收入(元) */
    private BigDecimal monthRevenue;

    /** 资源合规通过率(%) */
    private BigDecimal complianceRate;
}
