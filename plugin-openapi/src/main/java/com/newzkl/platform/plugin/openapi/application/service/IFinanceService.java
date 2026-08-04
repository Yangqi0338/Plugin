package com.newzkl.platform.plugin.openapi.application.service;

import java.math.BigDecimal;

public interface IFinanceService {

    void chargeSyncAmountRecord(Long accountId, BigDecimal amount);
}
