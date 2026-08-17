package com.newzkl.platform.plugin.bi.infrastructure.entity.channel;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 渠道商 HOME 总览宽表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.CHANNEL)
public class ChannelHomeDO extends BIBaseDO {

    /** 本月总订单金额(元) */
    private BigDecimal monthOrderAmount;

    /** 本月总订单数 */
    private Integer monthOrderCount;

    /** 今日订单数 */
    private Integer todayOrderCount;

    /** 采购金余额(元) */
    private BigDecimal purchaseBalance;

    /** 商品席位总数 */
    private Integer seatTotal;

    /** 已用商品席位 */
    private Integer seatUsed;
}
