package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 首页近7日趋势列表元素
 *
 * <p>折线图/柱状图数据点: 每个元素 = 一个日期, 含该日交易额(GMV)与上链量,
 * x 轴 = 日期, y 轴 = 指标值。</p>
 */
@Data
public class HomeTrendItemRes implements Serializable {

    /** 日期(MM-dd)
     */
    private String date;

    /** 交易额(GMV, 元)
     */
    private BigDecimal gmv;

    /** 上链量
     */
    private Integer onChainCount;
}
