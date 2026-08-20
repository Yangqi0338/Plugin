package com.newzkl.platform.plugin.bi.infrastructure.entity.partner;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 服务商 HOME 总览宽表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = AccountEnum.Client.PARTNER)
public class PartnerHomeDO extends BIBaseDO {

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
