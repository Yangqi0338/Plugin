package com.newzkl.platform.plugin.openapi.infrastructure.adapt.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.OrderFacade;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderAggVO;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderConfirmReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderFreightReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderRes;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderSubmitReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderVO;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import com.newzkl.platform.base.common.ddd.model.vo.ShipVO;
import com.newzkl.platform.plugin.openapi.domain.adapt.api.OrderApi;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 交易跨域出站端口实现
 *
 * <p>直调 Base biz-order {@code IOrderFacade}; 分页返回由 mybatis-plus
 * {@code Page} 转对外 {@code ApiPage}</p>
 *
 * @author KC
 */
@Component("openApiOrderApi")
public class OrderApiImpl implements OrderApi {

    @RpcReference
    private OrderFacade orderFacade;

    @Override
    public ApiOrderRes submit(Long accountId, ApiOrderSubmitReq orderReq) {
        return orderFacade.apiSubmitOrder(accountId, orderReq);
    }

    @Override
    public Page<ApiOrderVO> list(Long accountId, ApiOrderReq apiOrderReq) {
        return orderFacade.apiList(accountId, apiOrderReq);
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
    public Long freight(Long accountId, ApiOrderFreightReq orderReq) {
        return orderFacade.apiFreight(accountId, orderReq);
    }

    @Override
    public List<OrderStateVO> orderState(Long accountId, List<String> outOrderNoList) {
        return orderFacade.apiOrderState(accountId, outOrderNoList);
    }
}
