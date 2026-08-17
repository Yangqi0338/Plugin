package com.newzkl.platform.plugin.bi.model.res.admin;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 商品中心总量统计
 */
@Data
public class ProductSummaryRes implements Serializable {

    /** 商品总数(SPU) */
    private Integer spuCount;

    /** SKU总数 */
    private Integer skuCount;

    /** 已上链存证商品 */
    private Integer onChainGoodsCount;

    /** 一物一码绑定
     * @ext 上链后生成码, 一个商品状态
     */
    private Integer oneCodeBindCount;

    /** 今日新增商品
     * @ext 查实时表 spu_count
     */
    private Integer todayNewSpuCount;

    /** 存证率(%)
     * @ext 已上链存证商品/商品总数, 动态计算
     */
    public BigDecimal getOnChainRate() {
        if (spuCount == null || spuCount == 0 || onChainGoodsCount == null) {
            return null;
        }
        return new BigDecimal(onChainGoodsCount)
                .multiply(new BigDecimal("100"))
                .divide(new BigDecimal(spuCount), 1, RoundingMode.HALF_UP);
    }
}
