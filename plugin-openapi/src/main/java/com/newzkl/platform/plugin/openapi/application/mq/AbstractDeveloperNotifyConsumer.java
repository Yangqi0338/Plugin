package com.newzkl.platform.plugin.openapi.application.mq;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderDomain;
import com.newzkl.platform.base.common.core.mq.infrastructure.consumer.AbstractMessageMQPushConsumer;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import com.newzkl.platform.plugin.openapi.domain.repository.IDeveloperRepository;
import com.newzkl.platform.plugin.openapi.model.constants.Constants;
import com.newzkl.platform.plugin.openapi.model.util.SignatureUtil;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 开发者回调消费基类
 *
 * <p>模板方法固化「过滤 → 反查收件人 → 组装出站体 → 逐个 POST → 失败记账」五步,
 * 子类只声明自己的 tag/消费组与三个钩子。原 {@code DeveloperNotifyConsumer} 是
 * 中继第二跳的单一 consumer, 已随本 task 删除</p>
 *
 * @param <T> 业务事件负载类型 子类必须写出泛型实参 否则消息静默丢弃
 * @author KC
 */
@Slf4j
public abstract class AbstractDeveloperNotifyConsumer<T> extends AbstractMessageMQPushConsumer<T> {

    @Autowired
    private IDeveloperRepository developerRepository;

    @Autowired
    private ThirdPartyOrderDomain thirdPartyOrderDomain;

    @Override
    public final void remoteProcess(T message, Map<String, Object> extMap) {
        if (message == null || !accept(message)) {
            return;
        }
        List<Long> accountIdList = accountIdList(message);
        if (CollUtil.isEmpty(accountIdList)) {
            log.info("开发者通知跳过 无订阅者 event={}", message);
            return;
        }
        String requestBody = JSON.toJSONString(body(message, extMap));
        for (Long accountId : accountIdList) {
            DeveloperRes developer = developerRepository.developerVOByAccountId(accountId);
            if (developer == null || StrUtil.isBlank(developer.getNotifyAddress())) {
                log.warn("开发者通知跳过 无回调地址 accountId={}", accountId);
                continue;
            }
            pushOne(developer, requestBody);
        }
    }

    /**
     * 是否关心这条事件
     *
     * <p>默认全收 订单侧三个 consumer 覆写成 CHANNEL 闸门</p>
     *
     * @param message 业务事件负载
     * @return true 继续处理
     */
    protected boolean accept(T message) {
        return true;
    }

    /**
     * 反查本条事件的收件人账户ID列表
     *
     * @param message 业务事件负载
     * @return 收件人账户ID列表 空表示无人订阅
     */
    protected abstract List<Long> accountIdList(T message);

    /**
     * 组装出站 HTTP 契约体
     *
     * <p>返回值直接被序列化为请求 body 不再包任何信封</p>
     *
     * @param message 业务事件负载
     * @param extMap  MQ 扩展属性 仅供日志 不得据此改动 body 结构
     * @return 出站契约体对象
     */
    protected abstract Object body(T message, Map<String, Object> extMap);

    /**
     * 向单个开发者推送通知 失败落 record
     *
     * @param developer   开发者(含回调地址+appId)
     * @param requestBody 事件体 JSON
     */
    private void pushOne(DeveloperRes developer, String requestBody) {
        String url = developer.getNotifyAddress();
        String appId = developer.getAppId();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String randomNumber = UUID.randomUUID().toString();
        String sign = SignatureUtil.notifySign(requestBody, appId, developer.getSecret(), timeStamp, randomNumber);
        HttpRequest httpRequest = HttpRequest.post(url)
                .header(Constants.APP_ID, appId)
                .header(Constants.SIGN, sign)
                .header(Constants.TIME_STAMP, timeStamp)
                .header(Constants.RANDOM_NUMBER, randomNumber)
                .contentType(ContentType.JSON.getValue())
                .body(requestBody);
        try {
            HttpResponse httpResponse = httpRequest.execute();
            String responseBody = httpResponse.body();
            int httpStatus = httpResponse.getStatus();
            if (isNotificationSuccess(responseBody, httpStatus)) {
                log.info("开发者通知成功 url={} appId={} status={}", url, appId, httpStatus);
            } else {
                thirdPartyOrderDomain.recordAction(ThirdPartyOrderEnum.PlatformTypeEnum.LE_TAI, appId, ThirdPartyOrderEnum.Action.NOTIFY, null,
                        requestBody, responseBody, CommonEnum.RequestStatusEnum.FAILED, "通知失败");
                log.error("开发者通知失败 url={} appId={} status={} resp={}", url, appId, httpStatus, responseBody);
            }
        } catch (HttpException e) {
            thirdPartyOrderDomain.recordAction(ThirdPartyOrderEnum.PlatformTypeEnum.LE_TAI, appId, ThirdPartyOrderEnum.Action.NOTIFY, null,
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
