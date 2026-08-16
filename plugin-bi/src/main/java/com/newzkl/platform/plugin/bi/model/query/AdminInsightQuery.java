package com.newzkl.platform.plugin.bi.model.query;

import cn.hutool.core.date.DateTime;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据洞察查询(月度GMV/排行/关联趋势)
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdminInsightQuery extends QuerySupport {

    /** 开始时间(含) */
    private DateTime startTime;

    /** 结束时间(含), null 默认今天 */
    private DateTime endTime;
}
