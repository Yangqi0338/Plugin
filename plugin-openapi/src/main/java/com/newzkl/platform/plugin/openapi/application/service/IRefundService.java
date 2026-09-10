package com.newzkl.platform.plugin.openapi.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiFreightAddressReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundAggVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundFreightAddressVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundFreightReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundStateVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundSubmitReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundVO;

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

    Page<ApiRefundVO> list(Long accountId, ApiRefundReq apiRefundReq);

    ApiRefundAggVO detail(Long accountId, Long refundId);
}
