package com.newzkl.platform.plugin.openapi.application.mq;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.biz.order.domain.spi.ThirdPartyCompensator;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import com.newzkl.platform.plugin.openapi.domain.repository.IDeveloperRepository;
import com.newzkl.platform.plugin.openapi.model.constants.Constants;
import com.newzkl.platform.plugin.openapi.model.util.SignatureUtil;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 开发者通知补偿器(乐态)
 *
 * <p>重推 {@link AbstractDeveloperNotifyConsumer} 推送失败落库的记录 只负责发一次 HTTP
 * 状态与动作日志由派发器 {@code ThirdPartyOrderProcessor.compensation} 统一回写</p>
 *
 * @author KC
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeveloperNotifyCompensator implements ThirdPartyCompensator {

	private final IDeveloperRepository developerRepository;

	@Override
	public void compensation(ThirdPartyOrderRecordDTO request) {
		log.info("开始补偿开发者通知: {}", request.getBizOrderNo());
		String appId = request.getBizOrderNo();
		DeveloperRes developer = developerRepository.developerVOByAppId(appId);
		if (developer == null) {
			request.setRequestStatus(CommonEnum.RequestStatusEnum.FAILED);
			request.setErrorMessage("开发者不存在 appId=" + appId);
			log.warn("开发者通知补偿终止 开发者不存在 appId={}", appId);
			return;
		}
		String notifyAddress = developer.getNotifyAddress();
		if (StrUtil.isBlank(notifyAddress)) {
			request.setRequestStatus(CommonEnum.RequestStatusEnum.FAILED);
			request.setErrorMessage("开发者未配置回调地址 appId=" + appId);
			log.warn("开发者通知补偿终止 未配置回调地址 appId={}", appId);
			return;
		}
		try {
			String requestBody = request.getRequestJson();
			String timeStamp = String.valueOf(System.currentTimeMillis());
			String randomNumber = UUID.randomUUID().toString();
			String sign = SignatureUtil.notifySign(requestBody, appId, developer.getSecret(), timeStamp, randomNumber);
			HttpRequest httpRequest = HttpRequest.post(notifyAddress)
					.header(Constants.APP_ID, appId)
					.header(Constants.SIGN, sign)
					.header(Constants.TIME_STAMP, timeStamp)
					.header(Constants.RANDOM_NUMBER, randomNumber)
					.contentType(ContentType.JSON.getValue())
					.body(requestBody);
			HttpResponse httpResponse = httpRequest.execute();

			String responseBody = httpResponse.body();
			int httpStatus = httpResponse.getStatus();
			log.info("补偿任务 乐态 入参：{},返回参数：{}", requestBody, responseBody);
			if (isNotificationSuccess(responseBody, httpStatus)) {
				request.setRequestStatus(CommonEnum.RequestStatusEnum.SUCCESS);
				request.setResponseJson(responseBody);
				log.info("补偿任务 乐态 通知成功, url:{}, httpStatus:{}, response:{}",
						notifyAddress, httpStatus, responseBody);
			} else {
				request.setRequestStatus(CommonEnum.RequestStatusEnum.FAILED);
				request.setResponseJson(responseBody);
				request.setErrorMessage("通知失败 httpStatus=" + httpStatus);
			}
		} catch (Exception e) {
			request.setRequestStatus(CommonEnum.RequestStatusEnum.FAILED);
			request.setErrorMessage(e.getMessage());
			log.error("开发者通知补偿失败，调用API异常。业务号: {}", request.getBizOrderNo(), e);
		}
	}

	/**
	 * 判定通知是否成功 2xx + (响应 JSON code=200 或 响应含 ok)
	 *
	 * @param responseBody 响应体
	 * @param httpStatus   HTTP 状态码
	 * @return true 成功
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
			JSONObject jsonObject = JSON.parseObject(responseBody);
			Object codeObj = jsonObject.get("code");
			if (codeObj != null) {
				String code = codeObj.toString();
				if ("200".equals(code)) {
					log.info("响应为 JSON 且 code 为 {}，判定为通知成功。", code);
					return true;
				}
			}
			log.info("响应为 JSON，但 code 字段不为 200，判定为通知失败。");
			return false;
		} catch (Exception e) {
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
	public boolean supports(ThirdPartyOrderEnum.PlatformTypeEnum platformType) {
		return ThirdPartyOrderEnum.PlatformTypeEnum.LE_TAI == platformType;
	}
}
