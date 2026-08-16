package com.newzkl.platform.plugin.bi.domain.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.plugin.bi.domain.repository.StatRealtimeRepository;
import com.newzkl.platform.plugin.bi.infrastructure.entity.supplier.SupplierHomeDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.supplier.SupplierSettleTrendDO;
import com.newzkl.platform.plugin.bi.model.query.SupplierHomeQuery;
import com.newzkl.platform.plugin.bi.model.res.SupplierHomeRes;
import com.newzkl.platform.plugin.bi.model.res.SupplierSettleTrendItemRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 供应商侧(supplier)统计领域服务
 *
 * <p>按 client 维度划分: 本类只负责 {@code supplier} 端宽表的维度查询。</p>
 */
@Service
@RequiredArgsConstructor
public class SupplierStatDomain {

    private final StatRealtimeRepository realtimeRepo;

    /** 供应商 HOME 总览(实时 SUM) */
    public SupplierHomeRes home(SupplierHomeQuery query) {
        query.addSumField("on_shelf_sku_count", "pending_audit_count", "distributed_market_count",
                "covered_channel_count", "month_supply_order_count", "month_settle_amount", "settlement_rate");
        BizCountMap countMap = realtimeRepo.sum(SupplierHomeDO.class, new LambdaQueryWrapper<>(), query);
        SupplierHomeRes res = new SupplierHomeRes();
        if (countMap != null) {
            SupplierHomeDO d = CollUtil.getFirst(countMap.camelKeyCountMap().toList(SupplierHomeDO.class));
            TransferUtils.transfer(res, d);
        }
        // TODO: 环比(orderMomRatio)需日表对比, 待日归档后计算
        return res;
    }

    /** 供应商供货结算趋势(按月份) */
    public List<SupplierSettleTrendItemRes> settleTrend(SupplierHomeQuery query) {
        query.addField("month");
        query.addSumField("amount");
        query.addGroupField("month");
        query.initSortField("month", false);
        BizCountMap countMap = realtimeRepo.sum(SupplierSettleTrendDO.class, new LambdaQueryWrapper<>(), query);
        List<SupplierSettleTrendItemRes> list = new ArrayList<>();
        if (countMap != null) {
            List<SupplierSettleTrendDO> rows = countMap.camelKeyCountMap().toList(SupplierSettleTrendDO.class);
            for (SupplierSettleTrendDO row : rows) {
                SupplierSettleTrendItemRes item = new SupplierSettleTrendItemRes();
                item.setMonth(row.getMonth());
                item.setAmount(row.getAmount());
                list.add(item);
            }
        }
        return list;
    }
}
