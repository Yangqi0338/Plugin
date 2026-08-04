package com.newzkl.platform.plugin.openapi.application.service.impl;

import com.zkl.scm.openapi.application.service.IRefundService;
import com.zkl.scm.rpc.model.ApiPage;
import com.zkl.scm.sale.rpc.facade.IRefundFacade;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 商品
 * @date 2023/12/159:58
 */
@Service
public class RefundServiceImpl implements IRefundService {

    @DubboReference
    private IRefundFacade refundFacade;

    @Override
    public Long submit(Long accountId, ApiRefundSubmitReq refundSubmitReq) {
        return refundFacade.apiSubmit(accountId, refundSubmitReq);
    }

    @Override
    public void stop(Long accountId, Long refundId) {
        refundFacade.apiStop(accountId, refundId);
    }

    @Override
    public void pass(Long accountId, Long refundId) {
        refundFacade.apiPass(accountId, refundId);
    }

    @Override
    public void refuse(Long accountId, Long refundId) {
        refundFacade.apiRefuse(accountId, refundId);
    }

    @Override
    public void submitFreight(Long accountId, ApiRefundFreightReq refundFreightReq) {
        refundFacade.apiSubmitFreight(accountId, refundFreightReq);
    }

    @Override
    public ApiRefundFreightAddressVO freightAddress(Long accountId, ApiFreightAddressReq refundAddressInfoReq) {
        return refundFacade.apiFreightAddress(accountId, refundAddressInfoReq);
    }

    @Override
    public List<ApiRefundStateVO> refundState(Long accountId, List<Long> refundIdList) {
        return refundFacade.apiRefundState(accountId, refundIdList);
    }

    @Override
    public ApiPage<ApiRefundVO> list(Long accountId, ApiRefundReq apiRefundReq) {
        return refundFacade.apiList(accountId, apiRefundReq);
    }

    @Override
    public ApiRefundAggVO detail(Long accountId, Long refundId) {
        return refundFacade.apiDetail(accountId, refundId);
    }
}
