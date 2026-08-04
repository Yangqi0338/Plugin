package com.newzkl.platform.plugin.openapi.application.service;

import com.zkl.scm.rpc.model.ApiPage;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 售后
 * @date 2023/12/159:57
 */
public interface IRefundService {
    /**
     * 申请售后
     * @param accountId
     * @param refundSubmitReq
     */
    Long submit(Long accountId, ApiRefundSubmitReq refundSubmitReq);

    void stop(Long accountId, Long refundId);

    void pass(Long accountId, Long refundId);

    void refuse(Long accountId, Long refundId);

    void submitFreight(Long accountId, ApiRefundFreightReq refundFreightReq);

    ApiRefundFreightAddressVO freightAddress(Long accountId, ApiFreightAddressReq refundAddressInfoReq);

    List<ApiRefundStateVO> refundState(Long accountId, List<Long> refundIdList);

    ApiPage<ApiRefundVO> list(Long accountId, ApiRefundReq apiRefundReq);

    ApiRefundAggVO detail(Long accountId, Long refundId);
}
