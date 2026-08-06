package com.newzkl.platform.plugin.openapi.domain.adapt.api;

import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiFreightAddressReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundAggVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundFreightAddressVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundFreightReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundStateVO;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundSubmitReq;
import com.newzkl.platform.base.biz.order.facade.model.api.refund.ApiRefundVO;
import com.newzkl.platform.base.common.ddd.model.res.ApiPage;

import java.util.List;

/**
 * 售后跨域出站端口
 *
 * <p>openapi 插件对 biz-order 域售后能力的调用收敛于此, 对等旧
 * {@code @DubboReference IRefundFacade}</p>
 *
 * @author KC
 */
public interface RefundApi {

    /**
     * 申请售后
     *
     * @param accountId       账号主键
     * @param refundSubmitReq 售后请求
     * @return 售后主键
     */
    Long submit(Long accountId, ApiRefundSubmitReq refundSubmitReq);

    /**
     * 撤销售后
     *
     * @param accountId 账号主键
     * @param refundId  售后主键
     */
    void stop(Long accountId, Long refundId);

    /**
     * 售后通过
     *
     * @param accountId 账号主键
     * @param refundId  售后主键
     */
    void pass(Long accountId, Long refundId);

    /**
     * 售后拒绝
     *
     * @param accountId 账号主键
     * @param refundId  售后主键
     */
    void refuse(Long accountId, Long refundId);

    /**
     * 提交退货运单
     *
     * @param accountId        账号主键
     * @param refundFreightReq 运单请求
     */
    void submitFreight(Long accountId, ApiRefundFreightReq refundFreightReq);

    /**
     * 退货地址
     *
     * @param accountId           账号主键
     * @param refundAddressInfoReq 地址请求
     * @return 退货地址
     */
    ApiRefundFreightAddressVO freightAddress(Long accountId, ApiFreightAddressReq refundAddressInfoReq);

    /**
     * 售后状态
     *
     * @param accountId    账号主键
     * @param refundIdList 售后主键列表
     * @return 售后状态列表
     */
    List<ApiRefundStateVO> refundState(Long accountId, List<Long> refundIdList);

    /**
     * 售后列表
     *
     * @param accountId    账号主键
     * @param apiRefundReq 售后查询
     * @return 售后分页
     */
    ApiPage<ApiRefundVO> list(Long accountId, ApiRefundReq apiRefundReq);

    /**
     * 售后明细
     *
     * @param accountId 账号主键
     * @param refundId  售后主键
     * @return 售后聚合详情
     */
    ApiRefundAggVO detail(Long accountId, Long refundId);
}
