package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品中心 - 状态统计
 */
@Data
public class ProductStatusVO implements Serializable {

    /** 在售数 */
    private Integer onSaleCount;

    /** 待上架数 */
    private Integer pendingCount;

    /** 售罄数 */
    private Integer soldOutCount;
}