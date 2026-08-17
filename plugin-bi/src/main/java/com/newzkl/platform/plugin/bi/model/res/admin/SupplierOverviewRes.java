package com.newzkl.platform.plugin.bi.model.res.admin;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 供应商管理总览
 */
@Data
public class SupplierOverviewRes implements Serializable {

    /** 在管供应商(家) */
    private Integer supplierCount;

    /** 战略级(家) */
    private Integer strategicCount;

    /** 核心级(家) */
    private Integer coreCount;

    /** 供应商在售商品(款) */
    private Integer goodsOnSaleCount;

    /** 本月新增上传(款)
     * @ext 统计日表同名字段
     */
    private Integer goodsNewMonthCount;

    /** 30天动销率(%)
     * @ext 日表存储商品id集合(Text), 统计60d内出现两次以上且间隔<=30d, 代码计算
     */
    private BigDecimal sellRate30d;

    /** 应付货款在途(万元) */
    private BigDecimal payableInTransit;
}
