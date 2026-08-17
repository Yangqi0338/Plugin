package com.newzkl.platform.plugin.bi.domain.service;

import com.newzkl.platform.plugin.bi.model.query.AdminHomeOverviewQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminHomeRevenueQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminHomeTodoQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminHomeTradeQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminInsightQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminMemberQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminPaymentQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminProductQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminSupplierQuery;
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

public interface AdminStatDomain {
	/** 今日(实时表) + 昨日(日表) 实时概况 */
	HomeOverviewRes homeOverview(AdminHomeOverviewQuery query);
	
	/** 今日待办(TodoDO 各补偿增量 SUM, 钳为正整数) */
	HomeTodoRes homeTodo(AdminHomeTodoQuery query);
	
	/** 近7日 GMV + 上链量趋势(折线图列表): 历史日走日表, 今天走实时表 */
	List<HomeTrendItemRes> homeTrend(AdminHomeTradeQuery query);
	
	/** 各渠道今日收入(RevenueDO 每渠道字段 SUM), 占比由 Res getter 计算 */
	HomeChannelRevenueRes homeRevenue(AdminHomeRevenueQuery query);
	
	/** 商品总量统计(GoodsSummaryDO 各字段 SUM) */
	ProductSummaryRes productSummary(AdminProductQuery query);
	
	/** 商品分类统计(GoodsCategoryDO 按 categoryId 分组) */
	ProductCategoryRes productCategory(AdminProductQuery query);
	
	/**
	 * 品类占比(前5分类 + 其余归"其他")
	 *
	 * <p>复用 GoodsCategoryDO 分组数据: 按商品数量排序取前5, 剩余分类数量合并为"其他"。
	 * 与商品分类(productCategory)的区别: 前者全量分类, 本方法聚合为占比口径。</p>
	 */
	ProductCategoryRes categoryRatio(AdminProductQuery query);
	
	/** 商品状态统计(GoodsStatusDO 各字段 SUM) */
	ProductStatusRes productStatus(AdminProductQuery query);
	
	/** 会员总览(MemberSummaryDO SUM, 复购率 = repurchaseFlag SUM / memberCount) */
	MemberOverviewRes memberOverview(AdminMemberQuery query);
	
	/** 会员等级分布(MemberLevelDO 按 memberId 明细) */
	MemberLevelRes memberLevel(AdminMemberQuery query);
	
	/** 供应商总览(实时 SUM + 日表 goods_new_month_count + 动销率代码计算) */
	SupplierOverviewRes supplierOverview(AdminSupplierQuery query);
	
	/** 门店总览(StoreSummaryDO 各字段 SUM) */
	StoreOverviewRes storeOverview(AdminProductQuery query);
	
	/** 财务总览(FinanceSummaryDO 各字段 SUM) */
	FinanceOverviewRes financeOverview(AdminProductQuery query);
	
	/** 知链总览(EvidenceSummaryDO 各字段 SUM) */
	EvidenceOverviewRes evidenceOverview(AdminProductQuery query);
	
	/** 支付总览(实时 SUM, momRatio/feeRate 计算) */
	PaymentOverviewRes paymentOverview(AdminPaymentQuery query);
	
	/** 月度GMV趋势(按月聚合日表 TradeDO.amount) */
	InsightMonthGmvRes monthGmv(AdminInsightQuery query);
	
	/** 关联趋势(交易额/订单量/上链量): 复用 CorrelationDO, 不复用 homeTrend */
	List<HomeTrendItemRes> correlation(AdminInsightQuery query);
	
	/** 商品销售排行(GoodsRankDO 按 goodsId 分组排序) */
	RankGoodsRes goodsRank(AdminInsightQuery query);
	
	/** 店铺成交排行(StoreRankDO 按 storeId 分组排序) */
	RankStoreRes storeRank(AdminInsightQuery query);
}
