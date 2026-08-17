package com.newzkl.platform.plugin.bi.domain.service;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;

import java.math.BigDecimal;

public interface AdminEventDomain {
	/** 支付成功(任何支付): OverviewDO.gmv/payOrderCount + PaymentSummaryDO.payAmount */
	void onPaySuccess(CommonEnum.Client client, Long userId, BigDecimal amount);
	
	/** 商品订单支付成功: TradeDO + TodoDO + 排行 + CorrelationDO */
	void onGoodsPaySuccess(CommonEnum.Client client, Long userId, Long storeId, Long goodsId, BigDecimal amount);
	
	/** 退款通过: TodoDO(售后中+1) */
	void onRefundPass(CommonEnum.Client client, Long userId, BigDecimal refundAmount);
}
