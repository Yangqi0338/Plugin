package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 供应商管理总览宽表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = AccountEnum.Client.ADMIN)
public class SupplierSummaryDO extends BIBaseDO {

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

    /** 商品ID集合
     * @ext Text 字段, 日表存储, 用于计算 30d 动销率
     */
    private String goodsIdSet;

    /** 应付货款在途(万元) */
    private BigDecimal payableInTransit;
}
