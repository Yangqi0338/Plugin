package com.newzkl.platform.plugin.bi.application.service.impl;

import com.newzkl.platform.plugin.bi.application.service.ChannelService;
import com.newzkl.platform.plugin.bi.domain.service.ChannelStatDomain;
import com.newzkl.platform.plugin.bi.model.query.ChannelHomeQuery;
import com.newzkl.platform.plugin.bi.model.res.channel.HomeOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.channel.WeekTradeItemRes;
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
public class ChannelServiceImpl implements ChannelService {
    
    private final ChannelStatDomain channelStatDomain;

    // ==================== 渠道商 HOME ====================

    @Override
    public HomeOverviewRes channelHome() {
        ChannelHomeQuery query = new ChannelHomeQuery();
        return channelStatDomain.home(query);
    }

    /** 渠道商本周交易走势(销售额, 万元), 独立接口 */
    @Override
    public List<WeekTradeItemRes> channelWeekTrade() {
        return channelStatDomain.weekTrade(new ChannelHomeQuery());
    }
}
