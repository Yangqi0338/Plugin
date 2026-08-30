package com.newzkl.platform.plugin.openapi.domain.adapt.api;

import java.math.BigDecimal;

/**
 * 财务跨域出站端口
 *
 * <p>openapi 插件对 biz-finance 域钱包能力的调用收敛于此, 对等旧
 * {@code @DubboReference IAccountPurseApi}</p>
 *
 * @author KC
 */
public interface FinanceApi {

    /**
     * 渠道商下游充值金额同步
     *
     * @param accountId 渠道商账号主键
     * @param amount    同步金额, 单位元
     */
    void channelSyncByDownStream(Long accountId, BigDecimal amount);
}
