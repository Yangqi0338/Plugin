package com.newzkl.platform.plugin.openapi.application.service.impl;


import com.zkl.scm.finance.rpc.api.purse.IAccountPurseApi;
import com.zkl.scm.openapi.application.service.IFinanceService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FinanceServiceService implements IFinanceService {

    @DubboReference
    private IAccountPurseApi accountPurseApi;

    @Override
    public void chargeSyncAmountRecord(Long accountId, BigDecimal amount) {
        accountPurseApi.channelSyncByDownStream(accountId, amount);
    }
}
