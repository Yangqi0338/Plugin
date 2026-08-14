package com.newzkl.platform.plugin.bi.action.admin;

import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.bi.application.BiApplicationService;
import com.newzkl.platform.plugin.bi.model.res.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 平台BI控制器(所有统计接口归口)
 *
 * <p>按身份端分组: /bi/admin/* = 平台身份, /bi/supplier/* = 供应商身份(后续)。
 * 所有接口走 {@link BiApplicationService} 编排, 不直接调 Domain/DAO。</p>
 */
@RestController("biAdminController")
@RequestMapping("/bi/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BiApplicationService biAppService;

    // ==================== 首页工作台 ====================

    @PostMapping("/home/overview")
    public PlatformResult<HomeOverviewVO> overview() {
        return PlatformResult.success(biAppService.homeOverview());
    }

    @PostMapping("/home/todo")
    public PlatformResult<HomeTodoVO> todo() {
        return PlatformResult.success(biAppService.homeTodo());
    }

    @PostMapping("/home/trend")
    public PlatformResult<List<HomeTrendVO>> trend() {
        return PlatformResult.success(biAppService.homeTrend());
    }

    @PostMapping("/home/revenue")
    public PlatformResult<HomeChannelRevenueVO> revenue() {
        return PlatformResult.success(biAppService.homeRevenue());
    }

    // ==================== 商品中心 ====================

    @PostMapping("/product/summary")
    public PlatformResult<ProductSummaryVO> productSummary() {
        return PlatformResult.success(biAppService.productSummary());
    }

    @PostMapping("/product/category")
    public PlatformResult<ProductCategoryVO> productCategory() {
        return PlatformResult.success(biAppService.productCategory());
    }

    @PostMapping("/product/status")
    public PlatformResult<ProductStatusVO> productStatus() {
        return PlatformResult.success(biAppService.productStatus());
    }

    // ==================== 会员中心 ====================

    @PostMapping("/member/overview")
    public PlatformResult<MemberOverviewVO> memberOverview() {
        return PlatformResult.success(biAppService.memberOverview());
    }

    // ==================== 供应商管理 ====================

    @PostMapping("/supplier/overview")
    public PlatformResult<SupplierOverviewVO> supplierOverview() {
        return PlatformResult.success(biAppService.supplierOverview());
    }

    // ==================== 支付中心 ====================

    @PostMapping("/payment/overview")
    public PlatformResult<PaymentOverviewVO> paymentOverview() {
        return PlatformResult.success(biAppService.paymentOverview());
    }

    // ==================== 数据洞察 ====================

    @PostMapping("/insight/monthGmv")
    public PlatformResult<InsightMonthGmvVO> monthGmv() {
        return PlatformResult.success(biAppService.monthGmv());
    }

    @PostMapping("/insight/categoryRatio")
    public PlatformResult<InsightCategoryVO> categoryRatio() {
        return PlatformResult.success(biAppService.categoryRatio());
    }

    @PostMapping("/insight/correlation")
    public PlatformResult<HomeTrendVO> correlation() {
        return PlatformResult.success(biAppService.correlation());
    }

    // ==================== 排行 ====================

    @PostMapping("/rank/goodsTop5")
    public PlatformResult<RankGoodsVO> goodsTop5() {
        return PlatformResult.success(biAppService.goodsTop5());
    }

    @PostMapping("/rank/storeTop5")
    public PlatformResult<RankStoreVO> storeTop5() {
        return PlatformResult.success(biAppService.storeTop5());
    }
}