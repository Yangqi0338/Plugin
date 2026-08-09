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
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderStateVO;
import com.newzkl.platform.base.common.ddd.model.res.ApiPage;
import com.newzkl.platform.plugin.openapi.domain.adapt.api.OrderApi;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class OrderApiImpl implements OrderApi {

    private final OrderFacade orderFacade;

    @Override
    public ApiOrderRes submit(Long accountId, ApiOrderSubmitReq orderReq) {
        return orderFacade.apiSubmitOrder(accountId, orderReq);
    }

    @Override
    public ApiPage<ApiOrderVO> list(Long accountId, ApiOrderReq spuOrderQuery) {
        Page<ApiOrderVO> page = orderFacade.apiList(accountId, spuOrderQuery);
        return ApiPage.of(page.getRecords(), (int) page.getCurrent(), (int) page.getSize(), page.getTotal());
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
    public List<SpuOrderStateVO> orderState(Long accountId, List<String> outOrderNoList) {
        return orderFacade.apiOrderState(accountId, outOrderNoList);
    }
}
