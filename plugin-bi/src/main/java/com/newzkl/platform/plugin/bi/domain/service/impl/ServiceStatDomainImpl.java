package com.newzkl.platform.plugin.bi.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.plugin.bi.domain.adapt.repository.StatRealtimeRepository;
import com.newzkl.platform.plugin.bi.domain.service.ServiceStatDomain;
import com.newzkl.platform.plugin.bi.infrastructure.entity.partner.PartnerHomeDO;
import com.newzkl.platform.plugin.bi.model.query.ServiceHomeQuery;
import com.newzkl.platform.plugin.bi.model.res.partner.HomeOverviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 服务商侧(service)统计领域服务
 *
 * <p>按 client 维度划分: 本类只负责 {@code service} 端宽表的维度查询。</p>
 */
@Service
@RequiredArgsConstructor
public class ServiceStatDomainImpl implements ServiceStatDomain {

    private final StatRealtimeRepository realtimeRepo;

    /** 服务商 HOME 总览(实时 SUM) */
    @Override
    public HomeOverviewRes home(ServiceHomeQuery query) {
        query.addSumField("on_shelf_service_count", "month_new_count", "total_call_count",
                "covered_channel_count", "month_revenue", "compliance_rate");
        BizCountMap countMap = realtimeRepo.sum(PartnerHomeDO.class, new LambdaQueryWrapper<>(), query);
        HomeOverviewRes res = new HomeOverviewRes();
        if (countMap != null) {
            PartnerHomeDO d = CollUtil.getFirst(countMap.camelKeyCountMap().toList(PartnerHomeDO.class));
            TransferUtils.transfer(res, d);
        }
        return res;
    }
}
