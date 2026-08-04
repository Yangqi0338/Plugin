package com.newzkl.platform.plugin.audit.adapter;

import com.newzkl.platform.plugin.audit.port.OpenapiNotifyPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 开放平台通知适配器
 *
 * <p>Base goods 侧无开放平台通知能力, 降级空实现加告警日志, 渠道商品变更通知因此不外发, 待 openapi 基建补齐</p>
 *
 * @author KC
 */
@Slf4j
@Component("auditPluginOpenapiNotifyAdapter")
public class OpenapiNotifyAdapter implements OpenapiNotifyPort {

    @Override
    public void batchSend(List<Long> accountIds, Integer serviceType, Integer businessType, String eventInfoJson) {
        log.warn("TODO[infra-gap]: Base 缺 openapi 通知能力, serviceType={} businessType={} accountIds={} 降级不外发",
                serviceType, businessType, accountIds);
    }
}
