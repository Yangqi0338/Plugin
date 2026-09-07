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
import com.newzkl.platform.base.biz.order.facade.model.order.OrderCreateRpcCommand;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderItemRpcCommand;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateVO;
import com.newzkl.platform.base.biz.order.model.req.OrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.OrderItemCommand;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.vo.OrderVO;
import com.newzkl.platform.base.common.ddd.model.vo.ShipVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import com.newzkl.platform.base.common.ddd.model.res.ApiPage;
import com.newzkl.platform.plugin.openapi.domain.adapt.api.OrderApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

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
        OrderCreateRpcCommand orderCreateCommand = new OrderCreateRpcCommand();
        orderCreateCommand.setChannelId(accountId);
        orderCreateCommand.setOrderType(OrderEnum.OrderType.CHANNEL);
        // openapi 入口的单一律标乐态来源(轴A 订单来源) 主体恒为渠道商(SignatureFilter 已置 Identity.CHANNEL)
        orderCreateCommand.setPlatformType(ThirdPartyOrderEnum.PlatformTypeEnum.LE_TAI);
        orderCreateCommand.setShipVO(TransferUtils.transfer(orderReq, apiShipVO -> {
            ShipVO shipVO = new ShipVO();
            shipVO.setShipName(apiShipVO.getShipName());
            shipVO.setShipPhone(apiShipVO.getShipPhone());
            shipVO.setShipArea(apiShipVO.getShipArea());
            shipVO.setShipAddress(apiShipVO.getShipAddress());
            shipVO.setShipProvinceCode(apiShipVO.getShipProvinceCode());
            shipVO.setShipCityCode(apiShipVO.getShipCityCode());
            shipVO.setShipAreaCode(apiShipVO.getShipAreaCode());
            shipVO.setShipZipCode(apiShipVO.getShipZipCode());
            return shipVO;
        }));
        orderCreateCommand.setOrderGoodsList(TransferUtils.transfers(orderReq.getOrderGoodsList(), apiOrderSubmitItemReq -> {
            OrderItemRpcCommand orderItemCommand = new OrderItemRpcCommand();
            orderItemCommand.setSkuId(apiOrderSubmitItemReq.getSkuId());
            orderItemCommand.setCount(apiOrderSubmitItemReq.getCount());
            return orderItemCommand;
        }));
        orderCreateCommand.setRemark(orderReq.getRemark());
        orderCreateCommand.setOutOrderNo(orderReq.getOutOrderNo());
        return orderFacade.commitOrder(orderCreateCommand);
    }

    @Override
    public Page<ApiOrderVO> list(Long accountId, ApiOrderReq apiOrderReq) {
        //参数转换
        OrderQuery spuOrderQuery = TransferUtils.transfer(apiOrderReq, OrderQuery.class);
        //分页查询
//        Page<OrderVO> apiSpuOrderPageVOPage = queryService.orderVOList(spuOrderQuery);
//        return TransferUtils.transferPage(apiSpuOrderPageVOPage,ApiOrderVO.class);
        return null;
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
