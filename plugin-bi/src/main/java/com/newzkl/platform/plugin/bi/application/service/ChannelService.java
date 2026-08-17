package com.newzkl.platform.plugin.bi.application.service;

import com.newzkl.platform.plugin.bi.model.res.channel.HomeOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.channel.WeekTradeItemRes;

import java.util.List;

public interface ChannelService {
	HomeOverviewRes channelHome();
	
	/** 渠道商本周交易走势(销售额, 万元), 独立接口 */
	List<WeekTradeItemRes> channelWeekTrade();
}
