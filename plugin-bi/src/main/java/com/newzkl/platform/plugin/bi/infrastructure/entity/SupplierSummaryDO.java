package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 供应商管理·总览宽表
 *
 * <p>字段 = 供应商管理查询展示字段。
 * 实时表: dws_realtime_admin_supplier_summary | 日表: dws_day_admin_supplier_summary</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN, suffix = "supplier_summary")
public class SupplierSummaryDO extends BIBaseDO {

    /** 在管供应商(家) */
    private Integer supplierCount;

    /** 战略级(家) */
    private Integer strategicCount;

    /** 核心级(家) */
    private Integer coreCount;

    /** 供应商在售商品(款) */
    private Integer goodsOnSaleCount;

    /** 本月新增上传(款) */
    private Integer goodsNewMonthCount;

    /** 30天动销率(%) */
    private String sellRate30d;

    /** 应付货款在途(万元) */
    private BigDecimal payableInTransit;
}
