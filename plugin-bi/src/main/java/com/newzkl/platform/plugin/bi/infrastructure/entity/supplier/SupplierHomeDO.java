package com.newzkl.platform.plugin.bi.infrastructure.entity.supplier;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 供应商 HOME 总览宽表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.SUPPLIER)
public class SupplierHomeDO extends BIBaseDO {

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

    /** 本月结算金额(元) */
    private BigDecimal monthSettleAmount;

    /** 回款及时率(%) */
    private BigDecimal settlementRate;
}
