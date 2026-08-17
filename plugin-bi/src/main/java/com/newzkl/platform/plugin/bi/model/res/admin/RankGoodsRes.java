package com.newzkl.platform.plugin.bi.model.res.admin;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品销售排行
 * @ext 不存 rank, SQL 分组排序
 */
@Data
public class RankGoodsRes implements Serializable {

    /** 商品排行列表 */
    private List<GoodsRankItem> goodsList;

    /**
     * 商品排行项
     */
    @Data
    public static class GoodsRankItem {

        /** 商品ID */
        private Long goodsId;

        /** 销量(件) */
        private Integer salesCount;

        /** 销售额(元) */
        private BigDecimal salesAmount;
    }
}
