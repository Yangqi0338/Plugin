package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品中心状态统计
 */
@Data
public class ProductStatusRes implements Serializable {

    /** 在售
     * @ext 有库存且上架
     */
    private Integer onSaleCount;

    /** 售罄
     * @ext 库存为 0
     */
    private Integer soldOutCount;

    /** 已上架 */
    private Integer onShelfCount;

    /** 已下架 */
    private Integer offShelfCount;
}
