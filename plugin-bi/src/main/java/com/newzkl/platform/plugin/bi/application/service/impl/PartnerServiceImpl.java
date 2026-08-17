package com.newzkl.platform.plugin.bi.application.service.impl;

import com.newzkl.platform.plugin.bi.application.service.PartnerService;
import com.newzkl.platform.plugin.bi.domain.service.ServiceStatDomain;
import com.newzkl.platform.plugin.bi.model.query.ServiceHomeQuery;
import com.newzkl.platform.plugin.bi.model.res.partner.HomeOverviewRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * BI 应用编排服务
 *
 * <p>所有 BI 统计查询的编排入口, 组合 Domain 层 + 缓存 + 配置。
 * 按身份端路由: 平台/供应商/渠道商等。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {
    
    private final ServiceStatDomain serviceStatDomain;

    // ==================== 服务商 HOME ====================

    @Override
    public HomeOverviewRes serviceHome() {
        ServiceHomeQuery query = new ServiceHomeQuery();
        return serviceStatDomain.home(query);
    }
}
