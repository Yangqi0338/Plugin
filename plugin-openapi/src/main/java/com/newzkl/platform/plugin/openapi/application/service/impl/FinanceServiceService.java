package com.newzkl.platform.plugin.openapi.application.service.impl;


import com.newzkl.platform.plugin.openapi.application.service.IFinanceService;
import com.newzkl.platform.plugin.openapi.domain.adapt.api.FinanceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 财富同步服务
 *
 * <p>渠道商下游充值金额同步, 转调 biz-finance 钱包能力, 对等旧 scm
 * {@code IAccountPurseApi.channelSyncByDownStream}
 */
@Service("openApiFinanceService")
@RequiredArgsConstructor
public class FinanceServiceService implements IFinanceService {

    private final FinanceApi financeApi;

    @Override
    public void chargeSyncAmountRecord(Long accountId, BigDecimal amount) {
        financeApi.channelSyncByDownStream(accountId, amount);
    }
}
