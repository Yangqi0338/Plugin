package com.newzkl.platform.plugin.bi.model.query;

import cn.hutool.core.date.DateTime;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 首页营销渠道收入比例查询
 *
 * <p>只查实时宽表(今日各渠道收入 SUM), 无时间范围参数。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdminHomeRevenueQuery extends QuerySupport {
}
