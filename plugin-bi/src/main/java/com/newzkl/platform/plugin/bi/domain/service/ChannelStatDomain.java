package com.newzkl.platform.plugin.bi.domain.service;

import com.newzkl.platform.plugin.bi.model.query.ChannelHomeQuery;
import com.newzkl.platform.plugin.bi.model.res.channel.HomeOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.channel.WeekTradeItemRes;

import java.util.List;

public interface ChannelStatDomain {
	/** 渠道商 HOME 总览(实时 SUM) */
	HomeOverviewRes home(ChannelHomeQuery query);
	
	/** 渠道商本周交易走势(按星期) */
	List<WeekTradeItemRes> weekTrade(ChannelHomeQuery query);
}
