package com.newzkl.platform.plugin.bi.action.controller;

import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.bi.application.service.PartnerService;
import com.newzkl.platform.plugin.bi.model.res.partner.HomeOverviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务商BI控制器
 */
@RestController("biPartnerController")
@RequestMapping("/bi/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService biAppService;

    /** 服务商 HOME 总览 */
    @PostMapping("/home")
    public PlatformResult<HomeOverviewRes> home() {
        return PlatformResult.success(biAppService.serviceHome());
    }
}
