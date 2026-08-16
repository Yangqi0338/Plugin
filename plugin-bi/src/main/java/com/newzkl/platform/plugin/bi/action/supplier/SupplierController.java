package com.newzkl.platform.plugin.bi.action.supplier;

import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.bi.application.BiApplicationService;
import com.newzkl.platform.plugin.bi.model.res.SupplierHomeRes;
import com.newzkl.platform.plugin.bi.model.res.SupplierSettleTrendItemRes;
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

    private final BiApplicationService biAppService;

    /** 供应商 HOME 总览 */
    @PostMapping("/home")
    public PlatformResult<SupplierHomeRes> home() {
        return PlatformResult.success(biAppService.supplierHome());
    }

    /** 供应商供货结算趋势(万元) */
    @PostMapping("/settleTrend")
    public PlatformResult<List<SupplierSettleTrendItemRes>> settleTrend() {
        return PlatformResult.success(biAppService.supplierSettleTrend());
    }
}
