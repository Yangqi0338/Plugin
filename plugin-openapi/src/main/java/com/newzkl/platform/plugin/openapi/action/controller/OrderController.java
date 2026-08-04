package com.newzkl.platform.plugin.openapi.action.controller;

import com.newzkl.platform.plugin.openapi.action.cmd.OrderCmd;
import com.zkl.scm.developer.annotation.OpenApi;
import com.zkl.scm.developer.constants.Constants;
import com.zkl.scm.developer.utils.DeveloperContextUtil;
import com.zkl.scm.model.web.PlatformResult;
import com.zkl.scm.openapi.application.service.IOrderService;
import com.zkl.scm.rpc.model.ApiPage;
import com.zkl.scm.sale.rpc.model.order.SpuOrderStateVO;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController("开放平台-订单")
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
    public PlatformResult<Integer> freight(@Validated @RequestBody ApiOrderFreightReq orderCommand) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        Integer freight = orderService.freight(accountId, orderCommand);
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
    public PlatformResult<List<SpuOrderStateVO>> orderState(@RequestBody OrderCmd.OrderIdList orderIdList) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        return PlatformResult.success(orderService.orderState(accountId, orderIdList.getOutOrderNoList()));
    }



}
