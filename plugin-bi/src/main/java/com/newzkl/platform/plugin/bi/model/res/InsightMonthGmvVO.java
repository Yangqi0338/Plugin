package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.util.List;

/**
 * 数据洞察·月度GMV趋势(万元)
 */
@Data
public class InsightMonthGmvVO {

    /** 月份序列(3月/4月/...) */
    private List<String> monthList;

    /** 月度GMV(万元) */
    private List<Double> gmvList;
}
