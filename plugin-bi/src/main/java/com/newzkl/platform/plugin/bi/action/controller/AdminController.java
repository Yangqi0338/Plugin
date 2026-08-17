package com.newzkl.platform.plugin.bi.action.controller;

import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.bi.application.service.AdminService;
import com.newzkl.platform.plugin.bi.model.res.admin.EvidenceOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.admin.FinanceOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.admin.HomeChannelRevenueRes;
import com.newzkl.platform.plugin.bi.model.res.admin.HomeOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.admin.HomeTodoRes;
import com.newzkl.platform.plugin.bi.model.res.admin.HomeTrendItemRes;
import com.newzkl.platform.plugin.bi.model.res.admin.InsightMonthGmvRes;
import com.newzkl.platform.plugin.bi.model.res.admin.MemberOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.admin.PaymentOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.admin.ProductCategoryRes;
import com.newzkl.platform.plugin.bi.model.res.admin.ProductStatusRes;
import com.newzkl.platform.plugin.bi.model.res.admin.ProductSummaryRes;
import com.newzkl.platform.plugin.bi.model.res.admin.RankGoodsRes;
import com.newzkl.platform.plugin.bi.model.res.admin.RankStoreRes;
import com.newzkl.platform.plugin.bi.model.res.admin.StoreOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.admin.SupplierOverviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 平台BI控制器
 */
@RestController("biAdminController")
@RequestMapping("/bi/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService biAppService;

    // ==================== 首页工作台 ====================

    @PostMapping("/home/overview")
    public PlatformResult<HomeOverviewRes> overview() {
        return PlatformResult.success(biAppService.homeOverview());
    }

    @PostMapping("/home/todo")
    public PlatformResult<HomeTodoRes> todo() {
        return PlatformResult.success(biAppService.homeTodo());
    }

    @PostMapping("/home/trend")
    public PlatformResult<List<HomeTrendItemRes>> trend() {
        return PlatformResult.success(biAppService.homeTrend());
    }

    @PostMapping("/home/revenue")
    public PlatformResult<HomeChannelRevenueRes> revenue() {
        return PlatformResult.success(biAppService.homeRevenue());
    }

    // ==================== 商品中心 ====================

    @PostMapping("/product/summary")
    public PlatformResult<ProductSummaryRes> productSummary() {
        return PlatformResult.success(biAppService.productSummary());
    }

    @PostMapping("/product/category")
    public PlatformResult<ProductCategoryRes> productCategory() {
        return PlatformResult.success(biAppService.productCategory());
    }

    @PostMapping("/product/status")
    public PlatformResult<ProductStatusRes> productStatus() {
        return PlatformResult.success(biAppService.productStatus());
    }

    // ==================== 会员中心 ====================

    @PostMapping("/member/overview")
    public PlatformResult<MemberOverviewRes> memberOverview() {
        return PlatformResult.success(biAppService.memberOverview());
    }

    // ==================== 门店管理 ====================

    @PostMapping("/store/overview")
    public PlatformResult<StoreOverviewRes> storeOverview() {
        return PlatformResult.success(biAppService.storeOverview());
    }

    // ==================== 财务结算 ====================

    @PostMapping("/finance/overview")
    public PlatformResult<FinanceOverviewRes> financeOverview() {
        return PlatformResult.success(biAppService.financeOverview());
    }

    // ==================== 知链存证 ====================

    @PostMapping("/evidence/overview")
    public PlatformResult<EvidenceOverviewRes> evidenceOverview() {
        return PlatformResult.success(biAppService.evidenceOverview());
    }

    // ==================== 供应商管理 ====================

    @PostMapping("/supplier/overview")
    public PlatformResult<SupplierOverviewRes> supplierOverview() {
        return PlatformResult.success(biAppService.supplierOverview());
    }

    // ==================== 支付中心 ====================

    @PostMapping("/payment/overview")
    public PlatformResult<PaymentOverviewRes> paymentOverview() {
        return PlatformResult.success(biAppService.paymentOverview());
    }

    // ==================== 数据洞察 ====================

    @PostMapping("/insight/monthGmv")
    public PlatformResult<InsightMonthGmvRes> monthGmv() {
        return PlatformResult.success(biAppService.monthGmv());
    }

    @PostMapping("/insight/categoryRatio")
    public PlatformResult<ProductCategoryRes> categoryRatio() {
        return PlatformResult.success(biAppService.categoryRatio());
    }

    @PostMapping("/insight/correlation")
    public PlatformResult<List<HomeTrendItemRes>> correlation() {
        return PlatformResult.success(biAppService.correlation());
    }

    // ==================== 排行 ====================

    @PostMapping("/rank/goodsTop5")
    public PlatformResult<RankGoodsRes> goodsTop5() {
        return PlatformResult.success(biAppService.goodsTop5());
    }

    @PostMapping("/rank/storeTop5")
    public PlatformResult<RankStoreRes> storeTop5() {
        return PlatformResult.success(biAppService.storeTop5());
    }
}