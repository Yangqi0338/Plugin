package com.newzkl.platform.plugin.bi.application;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.plugin.bi.domain.constant.BiRedisKey;
import com.newzkl.platform.plugin.bi.domain.service.AdminStatDomain;
import com.newzkl.platform.plugin.bi.domain.service.ChannelStatDomain;
import com.newzkl.platform.plugin.bi.domain.service.ServiceStatDomain;
import com.newzkl.platform.plugin.bi.domain.service.SupplierStatDomain;
import com.newzkl.platform.plugin.bi.action.config.BiProperties;
import com.newzkl.platform.plugin.bi.model.query.AdminHomeOverviewQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminHomeRevenueQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminHomeTodoQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminHomeTradeQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminInsightQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminMemberQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminPaymentQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminProductQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminSupplierQuery;
import com.newzkl.platform.plugin.bi.model.query.ChannelHomeQuery;
import com.newzkl.platform.plugin.bi.model.query.ServiceHomeQuery;
import com.newzkl.platform.plugin.bi.model.query.SupplierHomeQuery;
import com.newzkl.platform.plugin.bi.model.res.HomeChannelRevenueRes;
import com.newzkl.platform.plugin.bi.model.res.HomeOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.HomeTodoRes;
import com.newzkl.platform.plugin.bi.model.res.HomeTrendItemRes;
import com.newzkl.platform.plugin.bi.model.res.InsightMonthGmvRes;
import com.newzkl.platform.plugin.bi.model.res.MemberLevelRes;
import com.newzkl.platform.plugin.bi.model.res.MemberOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.PaymentOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.ProductCategoryRes;
import com.newzkl.platform.plugin.bi.model.res.ProductStatusRes;
import com.newzkl.platform.plugin.bi.model.res.ProductSummaryRes;
import com.newzkl.platform.plugin.bi.model.res.StoreOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.FinanceOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.EvidenceOverviewRes;
import com.newzkl.platform.plugin.bi.model.res.RankGoodsRes;
import com.newzkl.platform.plugin.bi.model.res.RankStoreRes;
import com.newzkl.platform.plugin.bi.model.res.ChannelHomeRes;
import com.newzkl.platform.plugin.bi.model.res.ChannelWeekTradeItemRes;
import com.newzkl.platform.plugin.bi.model.res.ServiceHomeRes;
import com.newzkl.platform.plugin.bi.model.res.SupplierHomeRes;
import com.newzkl.platform.plugin.bi.model.res.SupplierSettleTrendItemRes;
import com.newzkl.platform.plugin.bi.model.res.SupplierOverviewRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * BI 应用编排服务
 *
 * <p>所有 BI 统计查询的编排入口, 组合 Domain 层 + 缓存 + 配置。
 * 按身份端路由: 平台/供应商/渠道商等。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BiApplicationService {

    private final AdminStatDomain adminStatDomain;
    private final SupplierStatDomain supplierStatDomain;
    private final ChannelStatDomain channelStatDomain;
    private final ServiceStatDomain serviceStatDomain;
    private final BiProperties biProperties;

    // ==================== 首页·实时概况 ====================

    public HomeOverviewRes homeOverview() {
        String cacheKey = BiRedisKey.OVERVIEW;
        HomeOverviewRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        DateTime today = DateUtil.beginOfDay(DateUtil.date());
        AdminHomeOverviewQuery adminHomeOverviewQuery = new AdminHomeOverviewQuery();
        adminHomeOverviewQuery.setStartTime(today);
        
        HomeOverviewRes vo = adminStatDomain.homeOverview(adminHomeOverviewQuery);

        try {
            RedisUtil.set(cacheKey, vo, biProperties.getOverviewCacheTtl());
        } catch (Exception e) {
            log.warn("写入 overview 缓存失败", e);
        }
        return vo;
    }

    // ==================== 首页·待办 ====================

    public HomeTodoRes homeTodo() {
        String cacheKey = BiRedisKey.TODO;
        HomeTodoRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        HomeTodoRes vo = adminStatDomain.homeTodo(new AdminHomeTodoQuery());

        try {
            RedisUtil.set(cacheKey, vo, biProperties.getTodoCacheTtl());
        } catch (Exception e) {
            log.warn("写入 todo 缓存失败", e);
        }
        return vo;
    }

    // ==================== 首页·近7日趋势 ====================

    public List<HomeTrendItemRes> homeTrend() {
        DateTime now = DateUtil.date();
        AdminHomeTradeQuery query = new AdminHomeTradeQuery();
        query.setStartTime(DateUtil.beginOfDay(DateUtil.offset(now, DateField.DAY_OF_YEAR, -6)));
        query.setEndTime(now);
        return adminStatDomain.homeTrend(query);
    }

    // ==================== 首页·营销渠道收入比例 ====================

    public HomeChannelRevenueRes homeRevenue() {
        return adminStatDomain.homeRevenue(new AdminHomeRevenueQuery());
    }

    /** 负数取 0(补偿增量可能为负) */
    private int nz(Money m) {
        int v = m == null || m.isNull() ? 0 : m.getAmount().intValue();
        return Math.max(v, 0);
    }

    // ==================== 商品中心 ====================

    public ProductSummaryRes productSummary() {
        String cacheKey = BiRedisKey.PRODUCT + "summary";
        ProductSummaryRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        ProductSummaryRes res = adminStatDomain.productSummary(new AdminProductQuery());
        try { RedisUtil.set(cacheKey, res, biProperties.getProductCacheTtl()); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    public ProductCategoryRes productCategory() {
        String cacheKey = BiRedisKey.PRODUCT + "category";
        ProductCategoryRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        ProductCategoryRes res = adminStatDomain.productCategory(new AdminProductQuery());
        try { RedisUtil.set(cacheKey, res, biProperties.getProductCacheTtl()); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    public ProductStatusRes productStatus() {
        String cacheKey = BiRedisKey.PRODUCT + "status";
        ProductStatusRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        ProductStatusRes res = adminStatDomain.productStatus(new AdminProductQuery());
        try { RedisUtil.set(cacheKey, res, biProperties.getProductCacheTtl()); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    // ==================== 会员中心 ====================

    public MemberOverviewRes memberOverview() {
        String cacheKey = BiRedisKey.MEMBER + "overview";
        MemberOverviewRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        MemberOverviewRes res = adminStatDomain.memberOverview(new AdminMemberQuery());
        try { RedisUtil.set(cacheKey, res, biProperties.getMemberCacheTtl()); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    public MemberLevelRes memberLevel() {
        return adminStatDomain.memberLevel(new AdminMemberQuery());
    }

    // ==================== 门店管理 ====================

    public StoreOverviewRes storeOverview() {
        String cacheKey = BiRedisKey.STORE;
        StoreOverviewRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        StoreOverviewRes res = adminStatDomain.storeOverview(new AdminProductQuery());
        try { RedisUtil.set(cacheKey, res, biProperties.getStoreCacheTtl()); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    // ==================== 财务结算 ====================

    public FinanceOverviewRes financeOverview() {
        String cacheKey = BiRedisKey.FINANCE;
        FinanceOverviewRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        FinanceOverviewRes res = adminStatDomain.financeOverview(new AdminProductQuery());
        try { RedisUtil.set(cacheKey, res, biProperties.getFinanceCacheTtl()); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    // ==================== 知链存证 ====================

    public EvidenceOverviewRes evidenceOverview() {
        String cacheKey = BiRedisKey.EVIDENCE;
        EvidenceOverviewRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        EvidenceOverviewRes res = adminStatDomain.evidenceOverview(new AdminProductQuery());
        try { RedisUtil.set(cacheKey, res, biProperties.getEvidenceCacheTtl()); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    // ==================== 供应商管理 ====================

    public SupplierOverviewRes supplierOverview() {
        String cacheKey = BiRedisKey.SUPPLIER;
        SupplierOverviewRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        SupplierOverviewRes res = adminStatDomain.supplierOverview(new AdminSupplierQuery());
        try { RedisUtil.set(cacheKey, res, biProperties.getSupplierCacheTtl()); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    // ==================== 支付中心 ====================

    public PaymentOverviewRes paymentOverview() {
        String cacheKey = BiRedisKey.PAYMENT;
        PaymentOverviewRes cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        PaymentOverviewRes res = adminStatDomain.paymentOverview(new AdminPaymentQuery());
        try { RedisUtil.set(cacheKey, res, biProperties.getPaymentCacheTtl()); } catch (Exception e) { log.warn("cache err", e); }
        return res;
    }

    // ==================== 数据洞察 ====================

    public InsightMonthGmvRes monthGmv() {
        DateTime now = DateUtil.date();
        AdminInsightQuery query = new AdminInsightQuery();
        query.setStartTime(DateUtil.beginOfMonth(DateUtil.offset(now, DateField.MONTH, -5)));
        query.setEndTime(now);
        return adminStatDomain.monthGmv(query);
    }

    /** 品类占比(前5分类 + 其余归"其他") */
    public ProductCategoryRes categoryRatio() {
        return adminStatDomain.categoryRatio(new AdminProductQuery());
    }

    public List<HomeTrendItemRes> correlation() {
        DateTime now = DateUtil.date();
        AdminInsightQuery query = new AdminInsightQuery();
        query.setStartTime(DateUtil.beginOfDay(DateUtil.offset(now, DateField.DAY_OF_YEAR, -6)));
        query.setEndTime(now);
        return adminStatDomain.correlation(query);
    }

    // ==================== 排行 ====================

    public RankGoodsRes goodsTop5() {
        AdminInsightQuery query = new AdminInsightQuery();
        return adminStatDomain.goodsRank(query);
    }

    public RankStoreRes storeTop5() {
        AdminInsightQuery query = new AdminInsightQuery();
        return adminStatDomain.storeRank(query);
    }

    // ==================== 供应商 HOME ====================

    public SupplierHomeRes supplierHome() {
        SupplierHomeQuery query = new SupplierHomeQuery();
        return supplierStatDomain.home(query);
    }

    /** 供应商供货结算趋势(万元), 独立接口 */
    public List<SupplierSettleTrendItemRes> supplierSettleTrend() {
        return supplierStatDomain.settleTrend(new SupplierHomeQuery());
    }

    // ==================== 渠道商 HOME ====================

    public ChannelHomeRes channelHome() {
        ChannelHomeQuery query = new ChannelHomeQuery();
        return channelStatDomain.home(query);
    }

    /** 渠道商本周交易走势(销售额, 万元), 独立接口 */
    public List<ChannelWeekTradeItemRes> channelWeekTrade() {
        return channelStatDomain.weekTrade(new ChannelHomeQuery());
    }

    // ==================== 服务商 HOME ====================

    public ServiceHomeRes serviceHome() {
        ServiceHomeQuery query = new ServiceHomeQuery();
        return serviceStatDomain.home(query);
    }
}
