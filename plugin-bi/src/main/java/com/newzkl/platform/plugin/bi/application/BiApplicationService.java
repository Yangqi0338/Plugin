package com.newzkl.platform.plugin.bi.application;

import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.plugin.bi.domain.constant.BiRedisKey;
import com.newzkl.platform.plugin.bi.domain.service.PriceStatDomain;
import com.newzkl.platform.plugin.bi.domain.service.QuantityStatDomain;
import com.newzkl.platform.plugin.bi.action.config.BiProperties;
import com.newzkl.platform.plugin.bi.model.res.HomeChannelRevenueVO;
import com.newzkl.platform.plugin.bi.model.res.HomeOverviewVO;
import com.newzkl.platform.plugin.bi.model.res.HomeTodoVO;
import com.newzkl.platform.plugin.bi.model.res.HomeTrendVO;
import com.newzkl.platform.plugin.bi.model.res.InsightCategoryVO;
import com.newzkl.platform.plugin.bi.model.res.InsightMonthGmvVO;
import com.newzkl.platform.plugin.bi.model.res.MemberOverviewVO;
import com.newzkl.platform.plugin.bi.model.res.PaymentOverviewVO;
import com.newzkl.platform.plugin.bi.model.res.ProductCategoryVO;
import com.newzkl.platform.plugin.bi.model.res.ProductStatusVO;
import com.newzkl.platform.plugin.bi.model.res.ProductSummaryVO;
import com.newzkl.platform.plugin.bi.model.res.RankGoodsVO;
import com.newzkl.platform.plugin.bi.model.res.RankStoreVO;
import com.newzkl.platform.plugin.bi.model.res.SupplierOverviewVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    private final PriceStatDomain priceStatDomain;
    private final QuantityStatDomain quantityStatDomain;
    private final BiProperties biProperties;

    // ==================== 首页·实时概况 ====================

    public HomeOverviewVO homeOverview() {
        String cacheKey = BiRedisKey.OVERVIEW;
        HomeOverviewVO cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        BigDecimal todayGmv = priceStatDomain.todayGmv();
        int orderCount = quantityStatDomain.todayOrderCount();
        int onChainCount = quantityStatDomain.todayOnChainCount();

        HomeOverviewVO vo = new HomeOverviewVO();
        vo.setTodayGmv(todayGmv);
        vo.setTodayOrderCount(orderCount);
        vo.setTodayEvidenceCount(onChainCount);

        try {
            RedisUtil.set(cacheKey, vo, biProperties.getOverviewCacheTtl());
        } catch (Exception e) {
            log.warn("写入 overview 缓存失败", e);
        }
        return vo;
    }

    // ==================== 首页·待办 ====================

    public HomeTodoVO homeTodo() {
        String cacheKey = BiRedisKey.TODO;
        HomeTodoVO cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        // TODO: 待办从各宽表 SUM 补偿增量, 当前为原型值
        HomeTodoVO vo = new HomeTodoVO();
        vo.setWaitPayCount(128);
        vo.setWaitDeliveryCount(46);
        vo.setRefundingCount(12);
        vo.setStockWarnCount(8);
        vo.setSoldOutCount(5);
        vo.setWaitVerifyCount(3);

        try {
            RedisUtil.set(cacheKey, vo, biProperties.getTodoCacheTtl());
        } catch (Exception e) {
            log.warn("写入 todo 缓存失败", e);
        }
        return vo;
    }

    // ==================== 首页·近7日趋势 ====================

    public List<HomeTrendVO> homeTrend() {
        List<String> dims = new ArrayList<>();
        Map<String, BigDecimal> gmvList = priceStatDomain.homeGmvTrend();
        List<Integer> orderList = quantityStatDomain.weekOrderCountTrend();
        List<Integer> onChainList = new ArrayList<>();

        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            dims.add(String.format("%02d-%02d", d.getMonthValue(), d.getDayOfMonth()));
        }
        
        for (int i = 0; i < 7; i++) {
            onChainList.add(0);
        }

        HomeTrendVO vo = new HomeTrendVO();
        vo.setDimensionList(dims);
        vo.setGmvList(gmvList);
        vo.setOnChainCountList(onChainList);
        return vo;
    }

    // ==================== 首页·营销渠道收入比例 ====================

    public HomeChannelRevenueVO homeRevenue() {
        HomeChannelRevenueVO vo = new HomeChannelRevenueVO();
        vo.setChannelList(Arrays.asList("微信小程序", "公众号", "H5商城", "抖音小程序", "APP", "门店收银台"));
        vo.setRatioList(Arrays.asList(38, 16, 12, 14, 11, 9));
        return vo;
    }

    // ==================== 商品中心 ====================

    public ProductSummaryVO productSummary() {
        // TODO: 从 GoodsTrend 宽表聚合
        ProductSummaryVO vo = new ProductSummaryVO();
        vo.setTotalSpu(10000);
        vo.setTotalGoods(4862);
        vo.setOnSaleGoods(8200);
        vo.setTotalStock(99999);
        return vo;
    }

    public ProductCategoryVO productCategory() {
        ProductCategoryVO vo = new ProductCategoryVO();
        vo.setCategoryList(Arrays.asList("生鲜果蔬", "粮油副食", "海鲜水产", "乳品冷饮", "休闲食品", "其他"));
        vo.setCountList(Arrays.asList(3400, 2200, 1200, 1000, 1300, 900));
        return vo;
    }

    public ProductStatusVO productStatus() {
        // TODO: 从 GoodsTrend 宽表聚合
        ProductStatusVO vo = new ProductStatusVO();
        vo.setOnSaleCount(8200);
        vo.setPendingCount(300);
        vo.setSoldOutCount(1500);
        return vo;
    }

    // ==================== 会员中心 ====================

    public MemberOverviewVO memberOverview() {
        // TODO: 从 MemberTrend 宽表聚合
        MemberOverviewVO vo = new MemberOverviewVO();
        vo.setMemberCount(74900L);
        vo.setTodayNew(326);
        vo.setVerifiedCount(52384L);
        vo.setVerifiedRate("69.9%");
        vo.setRepurchaseRate("38.6%");
        vo.setBalanceAmount(com.newzkl.platform.base.common.core.model.money.Money.of(1286540L));
        vo.setBalancePoints("862w");
        List<MemberOverviewVO.LevelItem> levels = new ArrayList<>();
        for (String[] item : new String[][]{
            {"普通会员", "42350"}, {"白银会员", "18620"}, {"黄金会员", "9840"},
            {"铂金会员", "3260"}, {"钻石会员", "830"},
        }) {
            MemberOverviewVO.LevelItem li = new MemberOverviewVO.LevelItem();
            li.setLevelName(item[0]);
            li.setCount(Integer.parseInt(item[1]));
            levels.add(li);
        }
        vo.setLevelList(levels);
        return vo;
    }

    // ==================== 供应商管理 ====================

    public SupplierOverviewVO supplierOverview() {
        String cacheKey = BiRedisKey.SUPPLIER;
        SupplierOverviewVO cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        // TODO: 从 SupplierTrend 宽表聚合, 当前为原型值
        SupplierOverviewVO vo = new SupplierOverviewVO();
        vo.setSupplierCount(186);
        vo.setStrategicCount(12);
        vo.setCoreCount(46);
        vo.setGoodsOnSaleCount(4862);
        vo.setGoodsNewMonthCount(328);
        vo.setSellRate30d("86.4%");
        vo.setClearAdvicePushed(true);
        vo.setPayableInTransit(new BigDecimal("486.2"));
        vo.setSettleTermDesc("T+7 账期自动结算");

        try {
            RedisUtil.set(cacheKey, vo, biProperties.getSupplierCacheTtl());
        } catch (Exception e) {
            log.warn("写入 supplier 缓存失败", e);
        }
        return vo;
    }

    // ==================== 支付中心 ====================

    public PaymentOverviewVO paymentOverview() {
        String cacheKey = BiRedisKey.PAYMENT;
        PaymentOverviewVO cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        // TODO: 从 PaymentTrend 宽表聚合, 当前为原型值
        PaymentOverviewVO vo = new PaymentOverviewVO();
        vo.setTodayPayAmount(new BigDecimal("338432.50"));
        vo.setMomRatio("+18.2%");
        vo.setTodayFeeAmount(new BigDecimal("1186.40"));
        vo.setFeeRate("0.35%");
        vo.setSplitCount(1842);
        vo.setSplitDesc("分销佣金/门店分账自动执行");
        vo.setReconDiffCount(0);
        vo.setZeroDiffDays(46);

        try {
            RedisUtil.set(cacheKey, vo, biProperties.getPaymentCacheTtl());
        } catch (Exception e) {
            log.warn("写入 payment 缓存失败", e);
        }
        return vo;
    }

    // ==================== 数据洞察 ====================

    public InsightMonthGmvVO monthGmv() {
        InsightMonthGmvVO vo = new InsightMonthGmvVO();
        vo.setMonthList(Arrays.asList("3月", "4月", "5月", "6月", "7月", "8月"));
        vo.setGmvList(Arrays.asList(180.0, 220.0, 260.0, 310.0, 350.0, 386.2));
        return vo;
    }

    public InsightCategoryVO categoryRatio() {
        InsightCategoryVO vo = new InsightCategoryVO();
        List<InsightCategoryVO.CategoryItem> list = new ArrayList<>();
        for (String[] item : new String[][]{
            {"生鲜果蔬", "34"}, {"粮油副食", "22"}, {"海鲜水产", "12"},
            {"乳品冷饮", "10"}, {"休闲食品", "13"}, {"其他", "9"},
        }) {
            InsightCategoryVO.CategoryItem ci = new InsightCategoryVO.CategoryItem();
            ci.setName(item[0]);
            ci.setRatio(Integer.parseInt(item[1]));
            list.add(ci);
        }
        vo.setCategoryList(list);
        return vo;
    }

    public HomeTrendVO correlation() {
        // 复用 homeTrend
        return homeTrend();
    }

    // ==================== 排行 ====================

    public RankGoodsVO goodsTop5() {
        RankGoodsVO vo = new RankGoodsVO();
        List<RankGoodsVO.GoodsRankItem> list = new ArrayList<>();
        for (String[] item : new String[][]{
            {"春禾安选·寒地黑土长粒香大米 10kg", "12430", "1193000"},
            {"产地仓直发·赣南脐橙 9斤装", "9872", "690000"},
            {"春禾安选·法库有机小米 5kg礼盒", "8214", "1051000"},
            {"销地仓直供·烟台红富士苹果 4.5kg", "6648", "398000"},
            {"春禾安选·林下散养土鸡蛋 60枚", "5210", "375000"},
        }) {
            RankGoodsVO.GoodsRankItem gi = new RankGoodsVO.GoodsRankItem();
            gi.setRank(list.size() + 1);
            gi.setGoodsName(item[0]);
            gi.setSalesCount(Integer.parseInt(item[1]));
            gi.setSalesAmount(new BigDecimal(item[2]));
            list.add(gi);
        }
        vo.setGoodsList(list);
        return vo;
    }

    public RankStoreVO storeTop5() {
        RankStoreVO vo = new RankStoreVO();
        List<RankStoreVO.StoreRankItem> list = new ArrayList<>();
        for (String[] item : new String[][]{
            {"中泽优选旗舰店", "1286000", "33"},
            {"春禾安选官方店", "986000", "26"},
            {"销地仓·杭州中心仓店", "742000", "19"},
            {"中泽集采企业店", "586000", "15"},
            {"春禾安选·徐州云仓店", "459000", "7"},
        }) {
            RankStoreVO.StoreRankItem si = new RankStoreVO.StoreRankItem();
            si.setRank(list.size() + 1);
            si.setStoreName(item[0]);
            si.setAmount(new BigDecimal(item[1]));
            si.setRatio(Integer.parseInt(item[2]));
            list.add(si);
        }
        vo.setStoreList(list);
        return vo;
    }
}
