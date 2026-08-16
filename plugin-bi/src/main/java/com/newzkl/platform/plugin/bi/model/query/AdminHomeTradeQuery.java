package com.newzkl.platform.plugin.bi.model.query;

import cn.hutool.core.date.DateTime;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 首页近7日交易与上链趋势查询
 *
 * <p>时间范围由 Application 组装(近7日每天), domain 按天走日表/实时表。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdminHomeTradeQuery extends QuerySupport {

    /** 开始日期(含)
     */
    private DateTime startTime;

    /** 结束日期(含)
     * @ext null 默认今天
     */
    private DateTime endTime;
}
