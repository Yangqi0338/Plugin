package com.newzkl.platform.plugin.openapi.application.service.impl;

import com.zkl.scm.openapi.application.service.IOrderService;
import com.zkl.scm.rpc.model.ApiPage;
import com.zkl.scm.sale.rpc.facade.IOrderFacade;
import com.zkl.scm.sale.rpc.model.order.SpuOrderStateVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 交易
 * @date 2024/1/1718:59
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @DubboReference
    private IOrderFacade orderFacade;

    @Override
    public ApiOrderRes submit(Long accountId, ApiOrderSubmitReq orderReq) {
        return orderFacade.apiSubmitOrder(accountId, orderReq);
    }

    @Override
    public ApiPage<ApiOrderVO> list(Long accountId, ApiOrderReq spuOrderQuery) {
        return orderFacade.apiList(accountId, spuOrderQuery);
    }

    @Override
    public ApiOrderAggVO detail(Long accountId, String outOrderNo) {
        return orderFacade.apiDetail(accountId, outOrderNo);
    }

    @Override
    public void confirm(Long accountId, ApiOrderConfirmReq confirmReq) {
        orderFacade.apiConfirm(accountId, confirmReq);
    }

    @Override
    public Integer freight(Long accountId, ApiOrderFreightReq orderReq) {
        return orderFacade.apiFreight(accountId, orderReq);
    }

    @Override
    public List<SpuOrderStateVO> orderState(Long accountId, List<String> outOrderNoList) {
        return orderFacade.apiOrderState(accountId, outOrderNoList);
    }
}
