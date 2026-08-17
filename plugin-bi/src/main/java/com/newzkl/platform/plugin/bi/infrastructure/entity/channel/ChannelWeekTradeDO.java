package com.newzkl.platform.plugin.bi.infrastructure.entity.channel;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 渠道商本周交易走势宽表(按星期, 销售额万元)
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.CHANNEL)
public class ChannelWeekTradeDO extends BIBaseDO {

    /** 星期(周一/周二/...) */
    private String weekDay;

    /** 销售额(万元) */
    private BigDecimal amount;
}
