package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品中心 - 总量统计
 */
@Data
public class ProductSummaryVO implements Serializable {

    /** 商品总数(SPU) */
    private Integer totalSpu;

    /** 货盘商品总量(款) */
    private Integer totalGoods;

    /** 在售商品 */
    private Integer onSaleGoods;

    /** 库存总量 */
    private Integer totalStock;
}