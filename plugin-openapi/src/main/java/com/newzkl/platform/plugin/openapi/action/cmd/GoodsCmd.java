package com.newzkl.platform.plugin.openapi.action.cmd;

import lombok.Data;

import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/159:43
 */
public class GoodsCmd {
    @Data
    public static class SpuIdReq {
        /**
         * spuId
         */
        private Long spuId;
    }
    @Data
    public static class SpuIdListReq {
        /**
         * spuId集合
         */
        private List<Long> spuIdList;
    }
    @Data
    public static class CategoryListReq {
        /**
         * 上级ID: 0或不传表示查询所有分类
         */
        private Long pid;
    }
    @Data
    public static class MarketGoodsInfoReq {
        /**
         * 商品id
         */
        private Long goodsId;
        /**
         * 商品标签
         */
        private String label;
    }
    @Data
    public static class GoldRealTimePriceReq {
        /**
         * 实时金价
         */
        private String price;
    }
    @Data
    public static class Header{
        String appId= "appId";
        String sign = "sign";
        String waitSignString = "waitSignString";
        String timeStamp= "timeStamp";
        String randomNumber= "randomNumber";
    }
}
