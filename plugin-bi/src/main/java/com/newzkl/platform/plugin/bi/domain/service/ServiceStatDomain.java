package com.newzkl.platform.plugin.bi.domain.service;

import com.newzkl.platform.plugin.bi.model.query.ServiceHomeQuery;
import com.newzkl.platform.plugin.bi.model.res.partner.HomeOverviewRes;

public interface ServiceStatDomain {
	/** 服务商 HOME 总览(实时 SUM) */
	HomeOverviewRes home(ServiceHomeQuery query);
}
