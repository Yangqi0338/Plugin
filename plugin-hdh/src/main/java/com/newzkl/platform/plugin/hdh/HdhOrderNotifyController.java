package com.newzkl.platform.plugin.hdh;

import com.newzkl.platform.base.biz.order.facade.model.hdh.OrderCallbackRequest;
import com.newzkl.platform.base.biz.order.facade.model.hdh.OrderCallbackResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会订货订单状态回调控制器
 *
 * <p>接收会订货平台推送的订单状态变更, 委托 {@link HdhEvent#handleStatusCallback} 完成
 * 三方状态码映射 → 订单状态机校验转换 → (到货态)按外部SKU反查内部SKU发货。
 * 迁移自 new-scm {@code HuiDingHuoNotifyController.orderStatusCallback} 的 HDH 同步分支。
 * 回调不做鉴权(三方直连), 由 HdhCallBackFilter 验签保证</p>
 *
 * <p>说明: new-scm 原端点含中泽(userOrderNum 以 "D" 开头)转发分支(转 PalletProperties.zzDOrderUrl),
 * 属 pallet 转发能力, 不在会订货订单同步范围, 本次不迁(见 deferred)。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/hdh/notify")
@RequiredArgsConstructor
public class HdhOrderNotifyController {

    private final HdhEvent hdhEvent;

    /**
     * 会订货订单状态回调 三方推送订单状态 → HdhEvent 状态机流转 + 发货
     *
     * @param callbackRequest 订单状态回调请求 含外部订单号 + 三方状态码 + 包裹列表
     * @return 处理结果 success=1 成功 / 0 失败
     */
    @PostMapping("/status/callback")
    public OrderCallbackResponse orderStatusCallback(@Valid @RequestBody OrderCallbackRequest callbackRequest) {
        log.info("收到会订货订单状态回调 outOrderNo={} thirdCode={}",
                callbackRequest == null ? null : callbackRequest.getUserOrderNum(),
                callbackRequest == null ? null : callbackRequest.getOrderStatusCode());
        OrderCallbackResponse response = new OrderCallbackResponse();
        response.setSuccess(0);
        try {
            boolean handled = hdhEvent.handleStatusCallback(callbackRequest);
            if (handled) {
                response.setSuccess(1);
                log.info("会订货订单回调处理成功 outOrderNo={}",
                        callbackRequest == null ? null : callbackRequest.getUserOrderNum());
            } else {
                log.warn("会订货订单回调处理失败 outOrderNo={}",
                        callbackRequest == null ? null : callbackRequest.getUserOrderNum());
            }
        } catch (Exception e) {
            log.error("会订货订单回调处理异常 request={}", callbackRequest, e);
        }
        return response;
    }
}
