package com.newzkl.platform.plugin.hdh;



import com.alibaba.fastjson2.JSONObject;

import com.newzkl.platform.base.common.ddd.facade.ThirdPartyOrderResult;
import com.newzkl.platform.plugin.hdh.model.req.HuiDingHuoCreateOrderReq;
import com.newzkl.platform.plugin.hdh.model.res.HuiDingHuoCreateOrderRes;


import java.util.List;

/**
 * 惠订货第三方订单结果适配器
 */
public class HuiDingHuoOrderResultAdapter implements ThirdPartyOrderResult {
	private final HuiDingHuoCreateOrderRes orderRes;

    private final HuiDingHuoCreateOrderReq orderReq;

	public HuiDingHuoOrderResultAdapter(HuiDingHuoCreateOrderRes orderRes, HuiDingHuoCreateOrderReq orderReq) {
		this.orderRes = orderRes;
        this.orderReq = orderReq;
    }

	@Override
	public String getOrderSn() {
		return orderRes.getData().getOrderNum(); // 惠订货订单号在data字段中
	}

	@Override
	public String getSkuIds() {
		// 惠订货响应只回订单号 不回三方 skuId 故无可映射的值(下单请求侧的 skuId 是我方外部商品 ID 非三方主键)
		return null;
	}

	@Override
	public List<? extends ThirdPartyOrderResult> getSubOrders() {
		// 惠订货一次下单只产生一张三方单 无嵌套子订单结构
		return null;
	}

    @Override
    public String getOrderRes() {
        return JSONObject.toJSONString(orderRes);
    }

    @Override
    public String getOrderReq() {
        return JSONObject.toJSONString(orderReq);
    }
}