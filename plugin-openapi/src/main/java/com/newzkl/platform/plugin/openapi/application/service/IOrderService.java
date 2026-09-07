package com.newzkl.platform.plugin.openapi.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderAggVO;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderConfirmReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderFreightReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderRes;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderSubmitReq;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderVO;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateVO;
import com.newzkl.platform.base.common.ddd.model.res.ApiPage;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 交易
 * @date 2023/12/159:57
 */
public interface IOrderService {
    /**
     * 下单
     *
     * @param accountId
     * @param orderReq
     * @return
     */
    ApiOrderRes submit(Long accountId, ApiOrderSubmitReq orderReq);
    /**
     * 列表
     *
     * @param accountId
     * @param spuOrderQuery
     * @return
     */
    Page<ApiOrderVO> list(Long accountId, ApiOrderReq spuOrderQuery);
    /**
     * 明细
     *
     * @param accountId
     * @param orderId
     * @return
     */
    ApiOrderAggVO detail(Long accountId, String orderId);
    /**
     * 确认收货
     * @param accountId
     * @param confirmReq
     */
    void confirm(Long accountId, ApiOrderConfirmReq confirmReq);
    /**
     * 查询运费
     *
     * @param accountId
     * @param orderReq
     * @return
     */
    Long freight(Long accountId, ApiOrderFreightReq orderReq);
    /**
     * 订单状态
     *
     * @param accountId
     * @param orderIdList
     * @return
     */
    List<OrderStateVO> orderState(Long accountId, List<String> orderIdList);
}
