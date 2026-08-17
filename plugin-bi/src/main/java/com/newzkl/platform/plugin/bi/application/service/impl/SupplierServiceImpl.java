package com.newzkl.platform.plugin.bi.application.service.impl;

import com.newzkl.platform.plugin.bi.application.service.SupplierService;
import com.newzkl.platform.plugin.bi.domain.service.SupplierStatDomain;
import com.newzkl.platform.plugin.bi.model.query.SupplierHomeQuery;
import com.newzkl.platform.plugin.bi.model.res.supplier.HomeOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.supplier.SettleTrendItemRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BI 应用编排服务
 *
 * <p>所有 BI 统计查询的编排入口, 组合 Domain 层 + 缓存 + 配置。
 * 按身份端路由: 平台/供应商/渠道商等。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    
    private final SupplierStatDomain supplierStatDomain;

    // ==================== 供应商 HOME ====================

    @Override
    public HomeOverviewRes supplierHome() {
        SupplierHomeQuery query = new SupplierHomeQuery();
        return supplierStatDomain.home(query);
    }

    /** 供应商供货结算趋势(万元), 独立接口 */
    @Override
    public List<SettleTrendItemRes> supplierSettleTrend() {
        return supplierStatDomain.settleTrend(new SupplierHomeQuery());
    }
}
