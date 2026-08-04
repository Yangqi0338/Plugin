package com.newzkl.platform.plugin.openapi.action.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.common.ddd.model.properties.PalletProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.JsonUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 会订货 回调
 */
@RestController
@RequestMapping("/hdh/notify")
@Slf4j
@RequiredArgsConstructor
public class HuiDingHuoNotifyController {

    @Autowired
    private HttpServletRequest request;
	
    private final IOrderFacade orderFacade;

    private final IGoodsService goodsService;

    /**
     * 会订货通知
     * @param detail
     * @return
     */
    @PostMapping("/hdhGoodsEvent")
    public PlatformResult<Void> hdhGoodsEvent(@Valid @RequestBody Item detail) {
	    log.info("会订货通知，请求参数：{}", detail);
	    try {
		    goodsService.hdhEvent(detail);
            log.info("HDH商品同步处理成功，外部订单号：{}", detail.getId());

            HttpClientUtils.httpPostRequest(PalletProperties.zzDGoodsUrl, JSONUtil.toJsonStr(detail));
            log.info("HDH商品同步转发成功，外部订单号：{}", detail.getId());
		    return PlatformResult.success();
	    } catch (Exception e) {
		    log.warn("HDH商品同步处理失败，外部订单号：{}", detail.getId(), e);
		    return PlatformResult.fail();
	    }
    }

	/**
	 * 订单状态回调接口
	 */
	@PostMapping("/status/callback")
	public OrderCallbackResponse orderStatusCallback(@Valid @RequestBody OrderCallbackRequest callbackRequest) {

		log.info("收到订单状态回调，外部订单号：{}，三方状态码：{}", callbackRequest.getUserOrderNum(), callbackRequest.getOrderStatusCode());

		// 初始化响应
		OrderCallbackResponse response = new OrderCallbackResponse();
		response.setSuccess(0);

        String userOrderNum = callbackRequest.getUserOrderNum();
        if (StrUtil.isBlank(userOrderNum)) {
            log.warn("回调外部订单号为空");
            return response;
        }

        // 中泽订单
        try {
            if (userOrderNum.startsWith("D")) {
                HashMap<String, Object> headers = MapUtil.of("SIGN", request.getHeader("SIGN"));
                String json = JsonUtils.loopSort4JsonString(JSONUtil.toJsonStr(callbackRequest), 10, true);
                log.info("订单转发地址：{}，请求头：{}，请求体：{}",PalletProperties.zzDOrderUrl, headers, json);
                HttpClientUtils.httpPostRequest(PalletProperties.zzDOrderUrl, headers, json);
                response.setSuccess(1);
                log.info("订单转发成功，外部订单号：{}", callbackRequest.getUserOrderNum());
                return response;
            }

			// 1. 调用Service处理业务逻辑
			boolean handleSuccess = orderFacade.handleStatusCallback(callbackRequest);
			if (handleSuccess) {
				response.setSuccess(1);
				log.info("订单回调处理成功，外部订单号：{}", callbackRequest.getUserOrderNum());
			} else {
				log.warn("订单回调处理失败，外部订单号：{}", callbackRequest.getUserOrderNum());
			}

		} catch (Exception e) {
			log.error("订单回调处理异常，请求参数：{}", callbackRequest, e);
		}

		return response;
	}

}