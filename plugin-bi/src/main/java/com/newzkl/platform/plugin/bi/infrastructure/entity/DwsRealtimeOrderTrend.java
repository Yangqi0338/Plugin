package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单趋势实时宽表(当日, 无时间维度字段)
 *
 * <p>与日宽表共享 {@link DwsOrderTrend} 主体, 唯一区别: 无 bizDate(恒为当前日)。
 * 只维护"今天"的补偿增量, 每日 00:30 存档进日宽表后清空。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dws_realtime_order_trend")
public class DwsRealtimeOrderTrend extends DwsOrderTrend {
}
