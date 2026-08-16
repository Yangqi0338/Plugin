package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 店铺成交排行
 * @ext 不存 rank, SQL 分组排序
 */
@Data
public class RankStoreRes implements Serializable {

    /** 店铺排行列表 */
    private List<StoreRankItem> storeList;

    /**
     * 店铺排行项
     */
    @Data
    public static class StoreRankItem {

        /** 店铺ID */
        private Long storeId;

        /** 成交额(元) */
        private BigDecimal salesAmount;
    }
}
