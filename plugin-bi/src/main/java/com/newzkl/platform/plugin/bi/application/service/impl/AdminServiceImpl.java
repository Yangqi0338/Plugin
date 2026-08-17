package com.newzkl.platform.plugin.bi.application.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.plugin.bi.action.config.BiProperties;
import com.newzkl.platform.plugin.bi.application.service.AdminService;
import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.plugin.bi.domain.service.AdminStatDomain;
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
public class AdminServiceImpl implements AdminService {

    private final AdminStatDomain adminStatDomain;

    // ==================== 首页·实时概况 ====================

    @Override
    public HomeOverviewRes homeOverview() {
        String cacheKey = RedisEnum.Key.BI_OVERVIEW.getCode();
        HomeOverviewRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        DateTime today = DateUtil.beginOfDay(DateUtil.date());
        AdminHomeOverviewQuery adminHomeOverviewQuery = new AdminHomeOverviewQuery();
        adminHomeOverviewQuery.setStartTime(today);
        
        HomeOverviewRes vo = adminStatDomain.homeOverview(adminHomeOverviewQuery);

        try {
            RedisUtil.set(cacheKey, vo, BiProperties.overviewCacheTtl);
        } catch (Exception e) {
            log.warn("写入 overview 缓存失败", e);
        }
        return vo;
    }

    // ==================== 首页·待办 ====================

    @Override
    public HomeTodoRes homeTodo() {
        String cacheKey = RedisEnum.Key.BI_TODO.getCode();
        HomeTodoRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        HomeTodoRes vo = adminStatDomain.homeTodo(new AdminHomeTodoQuery());

        try {
            RedisUtil.set(cacheKey, vo, BiProperties.todoCacheTtl);
        } catch (Exception e) {
            log.warn("写入 todo 缓存失败", e);
        }
        return vo;
    }

    // ==================== 首页·近7日趋势 ====================

    @Override
    public List<HomeTrendItemRes> homeTrend() {
        DateTime now = DateUtil.date();
        AdminHomeTradeQuery query = new AdminHomeTradeQuery();
        query.setStartTime(DateUtil.beginOfDay(DateUtil.offset(now, DateField.DAY_OF_YEAR, -6)));
        query.setEndTime(now);
        return adminStatDomain.homeTrend(query);
    }

    // ==================== 首页·营销渠道收入比例 ====================

    @Override
    public HomeChannelRevenueRes homeRevenue() {
        return adminStatDomain.homeRevenue(new AdminHomeRevenueQuery());
    }

    /** 负数取 0(补偿增量可能为负) */
    private int nz(Money m) {
        int v = m == null || m.isNull() ? 0 : m.getAmount().intValue();
        return Math.max(v, 0);
    }

    // ==================== 商品中心 ====================

    @Override
    public ProductSummaryRes productSummary() {
        String cacheKey = RedisEnum.Key.BI_PRODUCT.getCode() + "summary";
        ProductSummaryRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        ProductSummaryRes res = adminStatDomain.productSummary(new AdminProductQuery());
        try { RedisUtil.set(cacheKey, res, BiProperties.productCacheTtl); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    @Override
    public ProductCategoryRes productCategory() {
        String cacheKey = RedisEnum.Key.BI_PRODUCT.getCode() + "category";
        ProductCategoryRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        ProductCategoryRes res = adminStatDomain.productCategory(new AdminProductQuery());
        try { RedisUtil.set(cacheKey, res, BiProperties.productCacheTtl); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    @Override
    public ProductStatusRes productStatus() {
        String cacheKey = RedisEnum.Key.BI_PRODUCT.getCode() + "status";
        ProductStatusRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        ProductStatusRes res = adminStatDomain.productStatus(new AdminProductQuery());
        try { RedisUtil.set(cacheKey, res, BiProperties.productCacheTtl); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    // ==================== 会员中心 ====================

    @Override
    public MemberOverviewRes memberOverview() {
        String cacheKey = RedisEnum.Key.BI_MEMBER.getCode() + "overview";
        MemberOverviewRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        MemberOverviewRes res = adminStatDomain.memberOverview(new AdminMemberQuery());
        try { RedisUtil.set(cacheKey, res, BiProperties.memberCacheTtl); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    @Override
    public MemberLevelRes memberLevel() {
        return adminStatDomain.memberLevel(new AdminMemberQuery());
    }

    // ==================== 门店管理 ====================

    @Override
    public StoreOverviewRes storeOverview() {
        String cacheKey = RedisEnum.Key.BI_STORE.getCode();
        StoreOverviewRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        StoreOverviewRes res = adminStatDomain.storeOverview(new AdminProductQuery());
        try { RedisUtil.set(cacheKey, res, BiProperties.storeCacheTtl); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    // ==================== 财务结算 ====================

    @Override
    public FinanceOverviewRes financeOverview() {
        String cacheKey = RedisEnum.Key.BI_FINANCE.getCode();
        FinanceOverviewRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        FinanceOverviewRes res = adminStatDomain.financeOverview(new AdminProductQuery());
        try { RedisUtil.set(cacheKey, res, BiProperties.financeCacheTtl); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    // ==================== 知链存证 ====================

    @Override
    public EvidenceOverviewRes evidenceOverview() {
        String cacheKey = RedisEnum.Key.BI_EVIDENCE.getCode();
        EvidenceOverviewRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        EvidenceOverviewRes res = adminStatDomain.evidenceOverview(new AdminProductQuery());
        try { RedisUtil.set(cacheKey, res, BiProperties.evidenceCacheTtl); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    // ==================== 供应商管理 ====================

    @Override
    public SupplierOverviewRes supplierOverview() {
        String cacheKey = RedisEnum.Key.BI_SUPPLIER.getCode();
        SupplierOverviewRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        SupplierOverviewRes res = adminStatDomain.supplierOverview(new AdminSupplierQuery());
        try { RedisUtil.set(cacheKey, res, BiProperties.supplierCacheTtl); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    // ==================== 支付中心 ====================

    @Override
    public PaymentOverviewRes paymentOverview() {
        String cacheKey = RedisEnum.Key.BI_PAYMENT.getCode();
        PaymentOverviewRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        PaymentOverviewRes res = adminStatDomain.paymentOverview(new AdminPaymentQuery());
        try { RedisUtil.set(cacheKey, res, BiProperties.paymentCacheTtl); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    // ==================== 数据洞察 ====================

    @Override
    public InsightMonthGmvRes monthGmv() {
        DateTime now = DateUtil.date();
        AdminInsightQuery query = new AdminInsightQuery();
        query.setStartTime(DateUtil.beginOfMonth(DateUtil.offset(now, DateField.MONTH, -5)));
        query.setEndTime(now);
        return adminStatDomain.monthGmv(query);
    }

    /** 品类占比(前5分类 + 其余归"其他") */
    @Override
    public ProductCategoryRes categoryRatio() {
        return adminStatDomain.categoryRatio(new AdminProductQuery());
    }

    @Override
    public List<HomeTrendItemRes> correlation() {
        DateTime now = DateUtil.date();
        AdminInsightQuery query = new AdminInsightQuery();
        query.setStartTime(DateUtil.beginOfDay(DateUtil.offset(now, DateField.DAY_OF_YEAR, -6)));
        query.setEndTime(now);
        return adminStatDomain.correlation(query);
    }

    // ==================== 排行 ====================

    @Override
    public RankGoodsRes goodsTop5() {
        AdminInsightQuery query = new AdminInsightQuery();
        return adminStatDomain.goodsRank(query);
    }

    @Override
    public RankStoreRes storeTop5() {
        AdminInsightQuery query = new AdminInsightQuery();
        return adminStatDomain.storeRank(query);
    }
}
