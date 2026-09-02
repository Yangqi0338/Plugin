package com.newzkl.platform.plugin.openapi.application.service.impl;

import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderAggVO;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderConfirmReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderFreightReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderRes;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderSubmitReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderVO;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateVO;
import com.newzkl.platform.base.common.ddd.model.res.ApiPage;
import com.newzkl.platform.plugin.openapi.application.service.IOrderService;
import com.newzkl.platform.plugin.openapi.domain.adapt.api.OrderApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交易
 *
 * @author muc_fang
 */
@Service("openApiOrderService")
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderApi orderApi;

    @Override
    public ApiOrderRes submit(Long accountId, ApiOrderSubmitReq orderReq) {
        return orderApi.submit(accountId, orderReq);
    }

    @Override
    public ApiPage<ApiOrderVO> list(Long accountId, ApiOrderReq spuOrderQuery) {
        return orderApi.list(accountId, spuOrderQuery);
    }

    @Override
    public ApiOrderAggVO detail(Long accountId, String outOrderNo) {
        return orderApi.detail(accountId, outOrderNo);
    }

    @Override
    public void confirm(Long accountId, ApiOrderConfirmReq confirmReq) {
        orderApi.confirm(accountId, confirmReq);
    }

    @Override
    public Long freight(Long accountId, ApiOrderFreightReq orderReq) {
        return orderApi.freight(accountId, orderReq);
    }

    @Override
    public List<OrderStateVO> orderState(Long accountId, List<String> outOrderNoList) {
        return orderApi.orderState(accountId, outOrderNoList);
    }
}
