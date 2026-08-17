package com.newzkl.platform.plugin.bi.action.controller;

import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.bi.application.service.SupplierService;
import com.newzkl.platform.plugin.bi.model.res.supplier.HomeOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.supplier.SettleTrendItemRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 供应商BI控制器
 */
@RestController("biSupplierController")
@RequestMapping("/bi/supplier")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService biAppService;

    /** 供应商 HOME 总览 */
    @PostMapping("/home")
    public PlatformResult<HomeOverviewRes> home() {
        return PlatformResult.success(biAppService.supplierHome());
    }

    /** 供应商供货结算趋势(万元) */
    @PostMapping("/settleTrend")
    public PlatformResult<List<SettleTrendItemRes>> settleTrend() {
        return PlatformResult.success(biAppService.supplierSettleTrend());
    }
}
