package com.newzkl.platform.plugin.openapi.application.service.impl;

import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiFreightAddressReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundAggVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundFreightAddressVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundFreightReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundStateVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundSubmitReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundVO;
import com.newzkl.platform.base.common.ddd.model.res.ApiPage;
import com.newzkl.platform.plugin.openapi.application.service.IRefundService;
import com.newzkl.platform.plugin.openapi.domain.adapt.api.RefundApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 售后
 *
 * @author muc_fang
 */
@Service("openApiRefundService")
@RequiredArgsConstructor
public class RefundServiceImpl implements IRefundService {

    private final RefundApi refundApi;

    @Override
    public Long submit(Long accountId, ApiRefundSubmitReq refundSubmitReq) {
        return refundApi.submit(accountId, refundSubmitReq);
    }

    @Override
    public void stop(Long accountId, Long refundId) {
        refundApi.stop(accountId, refundId);
    }

    @Override
    public void pass(Long accountId, Long refundId) {
        refundApi.pass(accountId, refundId);
    }

    @Override
    public void refuse(Long accountId, Long refundId) {
        refundApi.refuse(accountId, refundId);
    }

    @Override
    public void submitFreight(Long accountId, ApiRefundFreightReq refundFreightReq) {
        refundApi.submitFreight(accountId, refundFreightReq);
    }

    @Override
    public ApiRefundFreightAddressVO freightAddress(Long accountId, ApiFreightAddressReq refundAddressInfoReq) {
        return refundApi.freightAddress(accountId, refundAddressInfoReq);
    }

    @Override
    public List<ApiRefundStateVO> refundState(Long accountId, List<Long> refundIdList) {
        return refundApi.refundState(accountId, refundIdList);
    }

    @Override
    public ApiPage<ApiRefundVO> list(Long accountId, ApiRefundReq apiRefundReq) {
        return refundApi.list(accountId, apiRefundReq);
    }

    @Override
    public ApiRefundAggVO detail(Long accountId, Long refundId) {
        return refundApi.detail(accountId, refundId);
    }
}
