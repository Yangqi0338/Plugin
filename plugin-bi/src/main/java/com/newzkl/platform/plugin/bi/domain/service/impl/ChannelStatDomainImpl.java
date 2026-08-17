package com.newzkl.platform.plugin.bi.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.plugin.bi.domain.adapt.repository.StatRealtimeRepository;
import com.newzkl.platform.plugin.bi.domain.service.ChannelStatDomain;
import com.newzkl.platform.plugin.bi.infrastructure.entity.channel.ChannelHomeDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.channel.ChannelWeekTradeDO;
import com.newzkl.platform.plugin.bi.model.query.ChannelHomeQuery;
import com.newzkl.platform.plugin.bi.model.res.channel.HomeOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.channel.WeekTradeItemRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 渠道商侧(channel)统计领域服务
 *
 * <p>按 client 维度划分: 本类只负责 {@code channel} 端宽表的维度查询。</p>
 */
@Service
@RequiredArgsConstructor
public class ChannelStatDomainImpl implements ChannelStatDomain {

    private final StatRealtimeRepository realtimeRepo;

    /** 渠道商 HOME 总览(实时 SUM) */
    @Override
    public HomeOverviewRes home(ChannelHomeQuery query) {
        query.addSumField("month_order_amount", "month_order_count", "today_order_count",
                "purchase_balance", "seat_total", "seat_used");
        BizCountMap countMap = realtimeRepo.sum(ChannelHomeDO.class, new LambdaQueryWrapper<>(), query);
        HomeOverviewRes res = new HomeOverviewRes();
        if (countMap != null) {
            ChannelHomeDO d = CollUtil.getFirst(countMap.camelKeyCountMap().toList(ChannelHomeDO.class));
            TransferUtils.transfer(res, d);
        }
        // TODO: 环比(orderAmountMomRatio)需日表对比, 待日归档后计算
        return res;
    }

    /** 渠道商本周交易走势(按星期) */
    @Override
    public List<WeekTradeItemRes> weekTrade(ChannelHomeQuery query) {
        query.addField("week_day");
        query.addSumField("amount");
        query.addGroupField("week_day");
        query.initSortField("week_day", false);
        BizCountMap countMap = realtimeRepo.sum(ChannelWeekTradeDO.class, new LambdaQueryWrapper<>(), query);
        List<WeekTradeItemRes> list = new ArrayList<>();
        if (countMap != null) {
            List<ChannelWeekTradeDO> rows = countMap.camelKeyCountMap().toList(ChannelWeekTradeDO.class);
            for (ChannelWeekTradeDO row : rows) {
                WeekTradeItemRes item = new WeekTradeItemRes();
                item.setWeekDay(row.getWeekDay());
                item.setAmount(row.getAmount());
                list.add(item);
            }
        }
        return list;
    }
}
