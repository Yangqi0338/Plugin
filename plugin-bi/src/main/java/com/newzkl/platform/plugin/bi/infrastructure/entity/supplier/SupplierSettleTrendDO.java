package com.newzkl.platform.plugin.bi.infrastructure.entity.supplier;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 供应商供货结算趋势宽表(按月, 万元)
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = AccountEnum.Client.SUPPLIER)
public class SupplierSettleTrendDO extends BIBaseDO {

    /** 月份(如 3月) */
    private String month;

    /** 结算金额(万元) */
    private BigDecimal amount;
}
