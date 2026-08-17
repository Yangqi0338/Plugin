package com.newzkl.platform.plugin.openapi.application.mq;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderDomain;
import com.newzkl.platform.base.common.core.mq.infrastructure.annotation.MQConsumer;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.core.mq.model.notify.NotifyEventMq;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import com.newzkl.platform.plugin.openapi.domain.repository.IDeveloperRepository;
import com.newzkl.platform.plugin.openapi.model.constants.NotifyContants;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;

/**
 * 开发者通知消费者
 *
 * <p>消费 {@link MQ.Tag#DEVELOPER_NOTIFY_EVENT} 事件, 按接收方 accountId 反查开发者回调地址,
 * HTTP POST 推送事件体; 推送失败落 {@code ThirdPartyOrderRecordDO}(经 recordAction 追加式)</p>
 *
 * @author KC
 */
@Slf4j
@MQConsumer(consumerGroup = MQ.Tag.DEVELOPER_NOTIFY_EVENT_MESSAGE, tag = {MQ.Tag.DEVELOPER_NOTIFY_EVENT})
public class DeveloperNotifyConsumer extends AbstractMessageMQPushConsumer<NotifyEventMq> {

    private static final String INTERFACE_NOTIFY = "notify";

    @Autowired
    private IDeveloperRepository developerRepository;
    @Autowired
    private ThirdPartyOrderDomain thirdPartyOrderDomain;

    @Override
    public void remoteProcess(NotifyEventMq mq, Map<String, Object> extMap) {
        if (mq == null || CollUtil.isEmpty(mq.getAccountIds())) {
            return;
        }
        String requestBody = mq.getEventContent();
        for (Long accountId : mq.getAccountIds()) {
            DeveloperRes developer = developerRepository.developerVOByAccountId(accountId);
            if (developer == null || StrUtil.isBlank(developer.getNotifyAddress())) {
                log.warn("开发者通知跳过 无回调地址 accountId={}", accountId);
                continue;
            }
            pushOne(developer, requestBody);
        }
    }

    /**
     * 向单个开发者推送通知 失败落 record
     *
     * @param developer   开发者(含回调地址+appId)
     * @param requestBody 事件体 JSON
     */
    private void pushOne(DeveloperRes developer, String requestBody) {
        String url = developer.getNotifyAddress();
        String appId = developer.getAppId();
        HttpRequest httpRequest = HttpRequest.post(url)
                .header(NotifyContants.sign, "sign")
                .header(NotifyContants.timeStamp, String.valueOf(System.currentTimeMillis()))
                .header(NotifyContants.randomNumber, UUID.randomUUID().toString())
                .contentType(ContentType.JSON.getValue())
                .body(requestBody);
        try {
            HttpResponse httpResponse = httpRequest.execute();
            String responseBody = httpResponse.body();
            int httpStatus = httpResponse.getStatus();
            if (isNotificationSuccess(responseBody, httpStatus)) {
                log.info("开发者通知成功 url={} appId={} status={}", url, appId, httpStatus);
            } else {
                thirdPartyOrderDomain.recordAction(ThirdPartyOrderEnum.PlatformTypeEnum.LE_TAI, appId, INTERFACE_NOTIFY, null,
                        requestBody, responseBody, CommonEnum.RequestStatusEnum.FAILED, "通知失败");
                log.error("开发者通知失败 url={} appId={} status={} resp={}", url, appId, httpStatus, responseBody);
            }
        } catch (HttpException e) {
            thirdPartyOrderDomain.recordAction(ThirdPartyOrderEnum.PlatformTypeEnum.LE_TAI, appId, INTERFACE_NOTIFY, null,
                    requestBody, "接口响应异常--调用失败", CommonEnum.RequestStatusEnum.FAILED, "通知失败");
            log.error("开发者通知异常 url={} appId={}", url, appId, e);
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
            return false;
        }
        if (StrUtil.isEmpty(responseBody)) {
            return false;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(responseBody);
            Object codeObj = jsonObject.get("code");
            return codeObj != null && "200".equals(codeObj.toString());
        } catch (Exception e) {
            return StrUtil.containsIgnoreCase(responseBody.trim(), "ok");
        }
    }
}
