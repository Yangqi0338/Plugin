package com.newzkl.platform.plugin.bi.action.service;

import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.bi.application.BiApplicationService;
import com.newzkl.platform.plugin.bi.model.res.ServiceHomeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务商BI控制器
 */
@RestController("biServiceController")
@RequestMapping("/bi/service")
@RequiredArgsConstructor
public class ServiceController {

    private final BiApplicationService biAppService;

    /** 服务商 HOME 总览 */
    @PostMapping("/home")
    public PlatformResult<ServiceHomeRes> home() {
        return PlatformResult.success(biAppService.serviceHome());
    }
}
