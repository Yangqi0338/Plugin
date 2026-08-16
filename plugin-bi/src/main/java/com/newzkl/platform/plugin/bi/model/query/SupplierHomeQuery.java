package com.newzkl.platform.plugin.bi.model.query;

import cn.hutool.core.date.DateTime;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 供应商 HOME 查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierHomeQuery extends QuerySupport {

    /** 开始时间(含) */
    private DateTime startTime;

    /** 结束时间(含) */
    private DateTime endTime;
}
