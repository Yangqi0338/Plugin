package com.newzkl.platform.plugin.bi.model.query;

import cn.hutool.core.date.DateTime;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 服务商 HOME 查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceHomeQuery extends QuerySupport {

    /** 开始时间(含) */
    private DateTime startTime;

    /** 结束时间(含) */
    private DateTime endTime;
}
