package com.newzkl.platform.plugin.openapi.domain.adapt.api;

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
 * 交易跨域出站端口
 *
 * <p>openapi 插件对 biz-order 域订单能力的调用收敛于此, 对等旧
 * {@code @DubboReference IOrderFacade}</p>
 *
 * @author KC
 */
public interface OrderApi {

    /**
     * 下单
     *
     * @param accountId 账号主键
     * @param orderReq  下单请求
     * @return 下单结果
     */
    ApiOrderRes submit(Long accountId, ApiOrderSubmitReq orderReq);

    /**
     * 订单列表
     *
     * @param accountId     账号主键
     * @param spuOrderQuery 订单查询
     * @return 订单分页
     */
    Page<ApiOrderVO> list(Long accountId, ApiOrderReq spuOrderQuery);

    /**
     * 订单明细
     *
     * @param accountId  账号主键
     * @param outOrderNo 外部订单号
     * @return 订单聚合详情
     */
    ApiOrderAggVO detail(Long accountId, String outOrderNo);

    /**
     * 确认收货
     *
     * @param accountId  账号主键
     * @param confirmReq 确认请求
     */
    void confirm(Long accountId, ApiOrderConfirmReq confirmReq);

    /**
     * 查询运费
     *
     * @param accountId 账号主键
     * @param orderReq  运费请求
     * @return 运费
     */
    Long freight(Long accountId, ApiOrderFreightReq orderReq);

    /**
     * 订单状态
     *
     * @param accountId      账号主键
     * @param outOrderNoList 外部订单号列表
     * @return 订单状态列表
     */
    List<OrderStateVO> orderState(Long accountId, List<String> outOrderNoList);
}
