package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 首页工作台 - 近7日交易与上链趋势
 *
 * <p>对应用户首页「近7日交易与上链趋势」: 交易额  + 上链量 三曲线。</p>
 */
@Data
public class HomeTrendVO implements Serializable {

    /** 维度列表(日期 yyyy-MM-dd) */
    private List<String> dimensionList;

    /** 交易额序列(元, 与 dimensionList 对齐) */
    private List<BigDecimal> gmvList;

    /** 上链量序列(与 dimensionList 对齐) */
    private List<Integer> onChainCountList;
}
