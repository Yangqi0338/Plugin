package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 渠道商本周交易走势项
 *
 * <p>折线图数据点: 星期(x 轴) + 销售额(万元, y 轴)。</p>
 */
@Data
public class ChannelWeekTradeItemRes implements Serializable {

    /** 星期(周一/周二/...) */
    private String weekDay;

    /** 销售额(万元) */
    private BigDecimal amount;
}
