package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品销售排行 TOP5(本月)
 */
@Data
public class RankGoodsVO {

    private List<GoodsRankItem> goodsList;

    @Data
    public static class GoodsRankItem {
        /** 排名 */
        private Integer rank;
        /** 商品名 */
        private String goodsName;
        /** 销量(件) */
        private Integer salesCount;
        /** 销售额(元) */
        private BigDecimal salesAmount;
    }
}
