package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 订单趋势日宽表(T+1)
 *
 * <p>与实时宽表共享 {@link DwsOrderTrend} 主体, 唯一区别: 多 bizDate(时间维度)。
 * 由实时宽表 00:30 存档而来(同字段 + bizDate), 供历史趋势/存量累计。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dws_day_order_trend")
public class DwsDayOrderTrend extends DwsOrderTrend {

    /** 时间维度: 业务日期 */
    private LocalDate bizDate;
}
