package com.newzkl.platform.plugin.bi.domain.service;

import com.newzkl.platform.plugin.bi.model.query.SupplierHomeQuery;
import com.newzkl.platform.plugin.bi.model.res.supplier.HomeOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.supplier.SettleTrendItemRes;

import java.util.List;

public interface SupplierStatDomain {
	/** 供应商 HOME 总览(实时 SUM) */
	HomeOverviewRes home(SupplierHomeQuery query);
	
	/** 供应商供货结算趋势(按月份) */
	List<SettleTrendItemRes> settleTrend(SupplierHomeQuery query);
}
