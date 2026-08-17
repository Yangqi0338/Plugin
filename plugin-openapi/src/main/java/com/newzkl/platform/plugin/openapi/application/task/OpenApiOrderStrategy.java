package com.newzkl.platform.plugin.openapi.application.task;


import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderDomain;
import com.newzkl.platform.base.biz.order.domain.spi.ThirdPartyOrderStrategy;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.biz.order.model.dto.OrderDTO;
import com.newzkl.platform.base.biz.order.model.dto.SkuCountDTO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderSkuVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.facade.ThirdPartyOrderResult;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import com.newzkl.platform.plugin.openapi.model.constants.NotifyContants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.JsonUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * 乐态第三方下单策略实现
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OpenApiOrderStrategy implements ThirdPartyOrderStrategy {
	
	private final ThirdPartyOrderDomain thirdPartyOrderDomain;
	
	@Override
	public ThirdPartyOrderResult create(List<OrderSkuVO> outGoods, OrderDTO order) {
		return null;
	}
	
	@Override
	public ThirdPartyOrderResult delivery(String outOrderNo, List<SkuCountDTO> skuCountDTOList, String expressCompanyName, String expressNo, Long channelId) {
		return null;
	}
	
	@Override
	public void compensation(ThirdPartyOrderRecordDTO request) {
		log.info("开始补偿惠订货订单: {}", request.getBizOrderNo());
		String sign = "sing";
		try {
			String requestBody = request.getRequestJson();
			HttpRequest httpRequest = HttpRequest.post(request.getInterfaceName())
					.header(NotifyContants.sign, sign)
					.header(NotifyContants.timeStamp, String.valueOf(System.currentTimeMillis()))
					.header(NotifyContants.randomNumber, UUID.randomUUID().toString())
					.contentType(ContentType.JSON.getValue())
					.body(requestBody);
			HttpResponse httpResponse = httpRequest.execute();
			
			String responseBody = httpResponse.body();
			int httpStatus = httpResponse.getStatus();
			log.info("补偿任务 乐态 入参：{},返回参数：{}", requestBody, httpResponse);
			if (isNotificationSuccess(responseBody, httpStatus)) {
				request.setRequestStatus(CommonEnum.RequestStatusEnum.SUCCESS);
				request.setResponseJson(responseBody);
				// TODO
//				thirdPartyOrderDomain.recordAction(request);
				log.info("补偿任务 乐态 通知成功, url:{}, dto:{}, httpStatus:{}, response:{}",
						request.getInterfaceName(),
						JsonUtils.toJson(requestBody),
						httpStatus,
						responseBody);
			}
		} catch (Exception e) {
			log.error("惠订货订单补偿失败，调用API异常。订单号: {}", request.getBizOrderNo(), e);
		}
	}
	
	/**
	 * 判断通知是否成功的辅助方法
	 *
	 */
	private boolean isNotificationSuccess(String responseBody, int httpStatus) {
		if (httpStatus < 200 || httpStatus >= 300) {
			log.info("HTTP 状态码不为 2xx，判定为通知失败。Status: {}", httpStatus);
			return false;
		}
		
		if (StringUtils.isEmpty(responseBody)) {
			log.info("响应体为空，判定为通知失败。");
			return false;
		}
		
		try {
			JSONObject jsonObject = JSONObject.parseObject(responseBody);
			Object codeObj = jsonObject.get("code");
			if (codeObj != null) {
				String code = codeObj.toString();
				if ("200".equals(code)) {
					log.info("响应为 JSON 且 code 为 {}，判定为通知成功。", code);
					return true;
				}
			}
			log.info("响应为 JSON，但 code 字段不为 200 或 0，判定为通知失败。");
			return false;
			
		}
		catch (Exception e) {
			log.info("响应体不是有效的 JSON 格式，尝试检查 'ok' 字符串。");
			if (StringUtils.containsIgnoreCase(responseBody.trim(), "ok")) {
				log.info("响应体包含 'ok' 字符串，判定为通知成功。");
				return true;
			}
		}
		
		log.info("响应体不包含 'ok' 且不是符合条件的 JSON，判定为通知失败。");
		return false;
	}
	
	@Override
	public boolean supports(Object type) {
		return ThirdPartyOrderEnum.PlatformTypeEnum.LE_TAI == type;
	}
}