package com.newzkl.platform.plugin.bi.model.query;

import cn.hutool.core.date.DateTime;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 首页待办查询
 *
 * <p>待办只查实时宽表(今日补偿增量 SUM), 无时间范围参数。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdminHomeTodoQuery extends QuerySupport {
}
