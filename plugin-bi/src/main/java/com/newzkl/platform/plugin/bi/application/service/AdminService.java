package com.newzkl.platform.plugin.bi.application.service;

import com.newzkl.platform.plugin.bi.model.res.admin.EvidenceOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.admin.FinanceOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.admin.HomeChannelRevenueRes;
import com.newzkl.platform.plugin.bi.model.res.admin.HomeOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.admin.HomeTodoRes;
import com.newzkl.platform.plugin.bi.model.res.admin.HomeTrendItemRes;
import com.newzkl.platform.plugin.bi.model.res.admin.InsightMonthGmvRes;
import com.newzkl.platform.plugin.bi.model.res.admin.MemberLevelRes;
import com.newzkl.platform.plugin.bi.model.res.admin.MemberOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.admin.PaymentOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.admin.ProductCategoryRes;
import com.newzkl.platform.plugin.bi.model.res.admin.ProductStatusRes;
import com.newzkl.platform.plugin.bi.model.res.admin.ProductSummaryRes;
import com.newzkl.platform.plugin.bi.model.res.admin.RankGoodsRes;
import com.newzkl.platform.plugin.bi.model.res.admin.RankStoreRes;
import com.newzkl.platform.plugin.bi.model.res.admin.StoreOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.admin.SupplierOverviewRes;

import java.util.List;

public interface AdminService {
	HomeOverviewRes homeOverview();
	
	HomeTodoRes homeTodo();
	
	List<HomeTrendItemRes> homeTrend();
	
	HomeChannelRevenueRes homeRevenue();
	
	ProductSummaryRes productSummary();
	
	ProductCategoryRes productCategory();
	
	ProductStatusRes productStatus();
	
	MemberOverviewRes memberOverview();
	
	MemberLevelRes memberLevel();
	
	StoreOverviewRes storeOverview();
	
	FinanceOverviewRes financeOverview();
	
	EvidenceOverviewRes evidenceOverview();
	
	SupplierOverviewRes supplierOverview();
	
	PaymentOverviewRes paymentOverview();
	
	InsightMonthGmvRes monthGmv();
	
	/** 品类占比(前5分类 + 其余归"其他") */
	ProductCategoryRes categoryRatio();
	
	List<HomeTrendItemRes> correlation();
	
	RankGoodsRes goodsTop5();
	
	RankStoreRes storeTop5();
}
