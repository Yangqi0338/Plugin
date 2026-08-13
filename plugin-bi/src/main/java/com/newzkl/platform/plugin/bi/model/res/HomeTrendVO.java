package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 首页工作台 - 近7日交易趋势
 *
 * <p>对应用户首页「近7日经营趋势」: 订单量 + GMV 双曲线。</p>
 */
@Data
public class HomeTrendVO implements Serializable {

    /** 维度列表(日期 yyyy-MM-dd) */
    private List<String> dimensionList;

    /** 订单量序列(与 dimensionList 对齐) */
    private List<Integer> orderCountList;

    /** GMV 序列(元, 与 dimensionList 对齐) */
    private List<BigDecimal> gmvList;
}
