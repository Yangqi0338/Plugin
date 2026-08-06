package com.newzkl.platform.plugin.openapi.action.controller;

import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiOrderRes;
import com.newzkl.platform.base.biz.order.facade.model.api.order.ApiSyncAmountRecordVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.openapi.application.service.IFinanceService;
import com.newzkl.platform.plugin.openapi.model.constants.Constants;
import com.newzkl.platform.plugin.openapi.model.util.DeveloperContextUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

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
