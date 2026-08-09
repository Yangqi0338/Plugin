package com.newzkl.platform.plugin.openapi.application.service.impl;


import com.newzkl.platform.plugin.openapi.application.service.IFinanceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 财富同步服务
 *
 * <p>D-30 能力缺口: 原 scm 走 {@code IAccountPurseApi.channelSyncByDownStream}
 * (渠道下游充值金额同步), Base 侧无对等出站能力, 暂留桩待 Base biz-account/purse 补齐对外 api
 */
@Service("openApiFinanceService")
public class FinanceServiceService implements IFinanceService {

    @Override
    public void chargeSyncAmountRecord(Long accountId, BigDecimal amount) {
        // D-30: Base 无 channelSyncByDownStream 对等能力, 待补
        throw new UnsupportedOperationException("chargeSyncAmountRecord 未接入: Base purse 缺 channelSyncByDownStream 能力 (D-30)");
    }
}
