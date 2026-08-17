package com.newzkl.platform.plugin.bi.application.service;

import com.newzkl.platform.plugin.bi.model.res.supplier.HomeOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.supplier.SettleTrendItemRes;

import java.util.List;

public interface SupplierService {
	HomeOverviewRes supplierHome();
	
	/** 供应商供货结算趋势(万元), 独立接口 */
	List<SettleTrendItemRes> supplierSettleTrend();
}
