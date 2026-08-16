package com.newzkl.platform.plugin.bi.domain.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.plugin.bi.domain.repository.StatRealtimeRepository;
import com.newzkl.platform.plugin.bi.infrastructure.entity.service.ServiceHomeDO;
import com.newzkl.platform.plugin.bi.model.query.ServiceHomeQuery;
import com.newzkl.platform.plugin.bi.model.res.ServiceHomeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 服务商侧(service)统计领域服务
 *
 * <p>按 client 维度划分: 本类只负责 {@code service} 端宽表的维度查询。</p>
 */
@Service
@RequiredArgsConstructor
public class ServiceStatDomain {

    private final StatRealtimeRepository realtimeRepo;

    /** 服务商 HOME 总览(实时 SUM) */
    public ServiceHomeRes home(ServiceHomeQuery query) {
        query.addSumField("on_shelf_service_count", "month_new_count", "total_call_count",
                "covered_channel_count", "month_revenue", "compliance_rate");
        BizCountMap countMap = realtimeRepo.sum(ServiceHomeDO.class, new LambdaQueryWrapper<>(), query);
        ServiceHomeRes res = new ServiceHomeRes();
        if (countMap != null) {
            ServiceHomeDO d = CollUtil.getFirst(countMap.camelKeyCountMap().toList(ServiceHomeDO.class));
            TransferUtils.transfer(res, d);
        }
        return res;
    }
}
