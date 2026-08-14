package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 店铺成交排行 TOP5(本月)
 */
@Data
public class RankStoreVO {

    private List<StoreRankItem> storeList;

    @Data
    public static class StoreRankItem {
        /** 排名 */
        private Integer rank;
        /** 店铺名 */
        private String storeName;
        /** 成交额(元) */
        private BigDecimal amount;
        /** 占比(%) */
        private Integer ratio;
    }
}
