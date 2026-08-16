package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 数据洞察月度GMV趋势
 * @ext 新增 gmv 统计表, 按 gmv 日表统计
 */
@Data
public class InsightMonthGmvRes implements Serializable {

    /** 月份列表(3月/4月/...) */
    private List<String> monthList;

    /** 月度GMV(元) */
    private List<BigDecimal> gmvList;
}
