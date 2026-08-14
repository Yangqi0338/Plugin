package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 供应商管理总览(平台身份)
 */
@Data
public class SupplierOverviewVO {

    /** 在管供应商(家) */
    private Integer supplierCount;

    /** 战略级供应商(家) */
    private Integer strategicCount;

    /** 核心级供应商(家) */
    private Integer coreCount;

    /** 供应商在售商品(款) */
    private Integer goodsOnSaleCount;

    /** 本月新增上传(款) */
    private Integer goodsNewMonthCount;

    /** 供应商30天动销率(%) */
    private String sellRate30d;

    /** 滞销商品已推送清库建议 */
    private Boolean clearAdvicePushed;

    /** 应付货款在途(万元) */
    private BigDecimal payableInTransit;

    /** 账期说明 */
    private String settleTermDesc;
}
