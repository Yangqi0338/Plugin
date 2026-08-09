package com.newzkl.platform.plugin.openapi.infrastructure.adapt.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.RefundFacade;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiFreightAddressReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundAggVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundFreightAddressVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundFreightReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundStateVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundSubmitReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundVO;
import com.newzkl.platform.base.common.ddd.model.res.ApiPage;
import com.newzkl.platform.plugin.openapi.domain.adapt.api.RefundApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 售后跨域出站端口实现
 *
 * <p>直调 Base biz-order {@code IRefundFacade}; 分页返回由 mybatis-plus
 * {@code Page} 转对外 {@code ApiPage}</p>
 *
 * @author KC
 */
@Component("openApiRefundApi")
@RequiredArgsConstructor
public class RefundApiImpl implements RefundApi {

    private final RefundFacade refundFacade;

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
        Page<ApiRefundVO> page = refundFacade.apiList(accountId, apiRefundReq);
        return ApiPage.of(page.getRecords(), (int) page.getCurrent(), (int) page.getSize(), page.getTotal());
    }

    @Override
    public ApiRefundAggVO detail(Long accountId, Long refundId) {
        return refundFacade.apiDetail(accountId, refundId);
    }
}
