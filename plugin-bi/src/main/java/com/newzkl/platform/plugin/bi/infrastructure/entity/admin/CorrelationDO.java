package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 数据洞察关联趋势宽表
 * @ext 交易额/订单量/上链量 三曲线
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = AccountEnum.Client.ADMIN)
public class CorrelationDO extends BIBaseDO {

    /** 交易额(元) */
    private BigDecimal amount;

    /** 订单量 */
    private Integer orderCount;

    /** 上链量 */
    private Integer onChainCount;
}
