package com.newzkl.platform.plugin.audit.port;

import java.util.List;

/**
 * 开放平台通知出站端口
 *
 * <p>向渠道批量发商品变更通知, Base goods 侧无对应能力, adapter 降级空实现加告警日志</p>
 *
 * @author KC
 */
public interface OpenapiNotifyPort {

    /**
     * 批量发通知
     *
     * @param accountIds 接收方账号主键列表
     * @param serviceType 服务类型
     * @param businessType 业务类型
     * @param eventInfoJson 事件内容 JSON
     */
    void batchSend(List<Long> accountIds, Integer serviceType, Integer businessType, String eventInfoJson);
}
