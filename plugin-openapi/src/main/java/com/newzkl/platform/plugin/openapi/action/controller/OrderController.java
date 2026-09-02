package com.newzkl.platform.plugin.openapi.action.controller;

import com.newzkl.platform.plugin.openapi.action.cmd.OrderCmd;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderAggVO;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderConfirmReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderFreightReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderRes;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderSubmitReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderVO;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateVO;
import com.newzkl.platform.base.common.ddd.model.res.ApiPage;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.openapi.model.constants.Constants;
import com.newzkl.platform.plugin.openapi.model.util.DeveloperContextUtil;
import com.newzkl.platform.plugin.openapi.application.service.IOrderService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 开放平台-订单
 * @author muc_fang
 */
@RestController("openApiOrderController")
@RequestMapping("/api/order")
@Setter(onMethod_ = @Autowired)
@Validated
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 创建订单
     * @param orderCommand
     * @return
     */
    @PostMapping("submit")
    public PlatformResult<ApiOrderRes> submit(@Validated @RequestBody ApiOrderSubmitReq orderCommand) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        return PlatformResult.success(orderService.submit(accountId, orderCommand));
    }



    /**
     * 查询下单运费
     * @param orderCommand
     * @return
     */
    @PostMapping("freight")
    public PlatformResult<Long> freight(@Validated @RequestBody ApiOrderFreightReq orderCommand) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        Long freight = orderService.freight(accountId, orderCommand);
        return PlatformResult.success(freight);
    }
    /**
     * 查询订单列表
     * @param orderReq
     * @return
     */
    @PostMapping("list")
    public PlatformResult<ApiPage<ApiOrderVO>> list(@Validated @RequestBody ApiOrderReq orderReq) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        ApiPage<ApiOrderVO> page = orderService.list(accountId, orderReq);
        return PlatformResult.success(page);
    }
    /**
     * 查询订单详情
     * @param orderId
     * @return
     */
    @PostMapping("detail")
    public PlatformResult<ApiOrderAggVO> detail(@Validated @RequestBody OrderCmd.OrderId orderId) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        ApiOrderAggVO spuOrderVO = orderService.detail(accountId, orderId.getOutOrderNo());
        return PlatformResult.success(spuOrderVO);
    }
    /**
     * 订单商品确认收货
     * @param confirmCommand
     * @return
     */
    @PostMapping("confirm")
    public PlatformResult<Void> confirm(@Validated @RequestBody ApiOrderConfirmReq confirmCommand) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        orderService.confirm(accountId, confirmCommand);
        return PlatformResult.success();
    }
    /**
     * 查询订单状态
     */
    @PostMapping("/orderState")
    public PlatformResult<List<OrderStateVO>> orderState(@RequestBody OrderCmd.OrderIdList orderIdList) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        return PlatformResult.success(orderService.orderState(accountId, orderIdList.getOutOrderNoList()));
    }



}
