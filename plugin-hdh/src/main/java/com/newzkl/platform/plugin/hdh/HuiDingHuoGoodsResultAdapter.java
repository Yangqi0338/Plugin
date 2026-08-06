package com.newzkl.platform.plugin.hdh;

import com.newzkl.platform.base.common.ddd.facade.ThirdPartyGoodsResult;

/**
 * 惠订货第三方商品同步结果适配器
 */
public class HuiDingHuoGoodsResultAdapter implements ThirdPartyGoodsResult {

    private final String outSpuId;

    private final String goodsReq;

    private final String goodsRes;

    public HuiDingHuoGoodsResultAdapter(String outSpuId, String goodsReq, String goodsRes) {
        this.outSpuId = outSpuId;
        this.goodsReq = goodsReq;
        this.goodsRes = goodsRes;
    }

    @Override
    public String getOutSpuId() {
        return outSpuId;
    }

    @Override
    public String getGoodsReq() {
        return goodsReq;
    }

    @Override
    public String getGoodsRes() {
        return goodsRes;
    }
}
