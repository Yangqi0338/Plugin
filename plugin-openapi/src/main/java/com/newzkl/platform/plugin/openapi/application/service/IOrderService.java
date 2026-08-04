package com.newzkl.platform.plugin.openapi.application.service;

import com.zkl.scm.rpc.model.ApiPage;
import com.zkl.scm.sale.rpc.model.order.SpuOrderStateVO;

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
    ApiPage<ApiOrderVO> list(Long accountId, ApiOrderReq spuOrderQuery);
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
    Integer freight(Long accountId, ApiOrderFreightReq orderReq);
    /**
     * 订单状态
     *
     * @param accountId
     * @param orderIdList
     * @return
     */
    List<SpuOrderStateVO> orderState(Long accountId, List<String> orderIdList);
}
