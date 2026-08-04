package com.newzkl.platform.plugin.openapi.action.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("开放平台-财富")
@RequestMapping("/api/finance")
@Setter(onMethod_ = @Autowired)
@Validated
public class FinanceController {

    @Resource
    private IFinanceService financeService;

    @PostMapping("chargeSyncAmountRecord")
    public PlatformResult<ApiOrderRes> chargeSyncAmountRecord(@Validated @RequestBody ApiSyncAmountRecordVO vo) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        financeService.chargeSyncAmountRecord(accountId, vo.getAmount());
        return PlatformResult.success();
    }

}
