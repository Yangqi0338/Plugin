package com.newzkl.platform.plugin.hdh;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.order.facade.OrderFacade;
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
 * <p>接收会订货平台推送的订单状态变更, 委托 {@link OrderFacade#handleStatusCallback} 完成
 * 三方状态码映射 → 订单状态机校验转换 → (到货态)按外部SKU反查内部SKU发货。
 * 迁移自 new-scm {@code HuiDingHuoNotifyController.orderStatusCallback} 的 HDH 同步分支。
 * 回调不做鉴权(三方直连), 由 HdhCallBackFilter 验签保证</p>
 *
 * <p>跨域约束: plugin 不得直连 biz-order domain, 回调业务逻辑落 biz-order
 * {@code OrderFacadeImpl.handleStatusCallback}, 本控制器仅经 facade 转发 (恢复 slug21 设计原意)。</p>
 *
 * <p>中泽 D 平台转发: userOrderNum 以 "D" 开头的订单属中泽 D 平台采购单, 不经本平台订单域处理,
 * 直接 HTTP 转发至 {@code PalletProperties.zzDOrderUrl} (透传原始报文)。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/hdh/notify")
@RequiredArgsConstructor
public class HdhOrderNotifyController {

    private final OrderFacade orderFacade;

    /**
     * 会订货订单状态回调 三方推送订单状态 → OrderFacade 状态机流转 + 发货
     *
     * <p>中泽分支: userOrderNum 以 "D" 开头 → 直接转发至 PalletProperties.zzDOrderUrl, 不进本平台订单域。</p>
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
            // 中泽 D 平台采购单: userOrderNum 以 "D" 开头, 转发至 zzDOrderUrl
            // TODO[deferred]: zzDOrderUrl 需从配置读取, PalletProperties 无此静态字段, 暂用空串占位
            if (callbackRequest != null
                    && callbackRequest.getUserOrderNum() != null
                    && callbackRequest.getUserOrderNum().startsWith("D")) {
                String zzDOrderUrl = "";
                log.info("中泽D平台订单回调转发 outOrderNo={} url={}", callbackRequest.getUserOrderNum(), zzDOrderUrl);
                HttpRequest.post(zzDOrderUrl)
                        .contentType("application/json")
                        .body(JSONUtil.toJsonStr(callbackRequest))
                        .timeout(10000)
                        .execute()
                        .body();
                response.setSuccess(1);
                return response;
            }
            boolean handled = orderFacade.handleStatusCallback(callbackRequest);
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

