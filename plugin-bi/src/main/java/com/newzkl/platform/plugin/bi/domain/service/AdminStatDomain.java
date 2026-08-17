package com.newzkl.platform.plugin.bi.domain.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.support.BaseQueryWrapper;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.plugin.bi.domain.repository.StatDayRepository;
import com.newzkl.platform.plugin.bi.domain.repository.StatRealtimeRepository;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.CorrelationDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.EvidenceSummaryDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.FinanceSummaryDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.StoreSummaryDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.GoodsCategoryDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.GoodsRankDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.GoodsStatusDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.GoodsSummaryDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.MemberLevelDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.MemberSummaryDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.OverviewDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.PaymentSummaryDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.RevenueDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.StoreRankDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.SupplierSummaryDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.TodoDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.TradeDO;
import com.newzkl.platform.plugin.bi.model.query.AdminHomeOverviewQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminHomeRevenueQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminHomeTodoQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminHomeTradeQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminInsightQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminMemberQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminPaymentQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminProductQuery;
import com.newzkl.platform.plugin.bi.model.query.AdminSupplierQuery;
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
import com.newzkl.platform.plugin.bi.model.res.SupplierOverviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 平台侧(admin)统计领域服务
 *
 * <p>按 client 维度划分: 本类只负责 {@code admin} 端宽表的维度查询。
 * Application 组装 Query 传入, domain 按 Query 分表(实时/日)调 Repository 组装 Res 返回;
 * 查询字段通过 QuerySupport.addSumField 声明, 复用 BizCountMap + TransferUtils。</p>
 */
@Service
@RequiredArgsConstructor
public class AdminStatDomain {

    private final StatRealtimeRepository realtimeRepo;
    private final StatDayRepository dayRepo;

    // ==================== 实时概况 ====================

    /** 今日(实时表) + 昨日(日表) 实时概况 */
    public HomeOverviewRes homeOverview(AdminHomeOverviewQuery query) {
        DateTime startTime = query.getStartTime();
        DateTime yesterday = startTime.offsetNew(DateField.DAY_OF_YEAR, -1);
        query.addSumField("gmv", "pay_order_count", "member_count", "evidence_count");

        HomeOverviewRes overviewVO = new HomeOverviewRes();
        BizCountMap todayCountMap = realtimeRepo.sum(OverviewDO.class, new LambdaQueryWrapper<>(), query);
        if (todayCountMap != null) {
            OverviewDO overviewDO = CollUtil.getFirst(todayCountMap.camelKeyCountMap().toList(OverviewDO.class));
            TransferUtils.transfer(overviewVO, overviewDO, cn.hutool.core.bean.copier.CopyOptions.create()
                    .setFieldNameEditor(it -> "today" + StrUtil.upperFirst(it)));
        }

        QueryWrapper<OverviewDO> queryWrapper = new BaseQueryWrapper<OverviewDO>()
                .between("biz_date", yesterday, startTime);
        BizCountMap yesterdayCountMap = dayRepo.sum(OverviewDO.class, queryWrapper, query);
        if (yesterdayCountMap != null) {
            OverviewDO overviewDO = CollUtil.getFirst(yesterdayCountMap.camelKeyCountMap().toList(OverviewDO.class));
            TransferUtils.transfer(overviewVO, overviewDO, cn.hutool.core.bean.copier.CopyOptions.create()
                    .setFieldNameEditor(it -> "yesterday" + StrUtil.upperFirst(it)));
        }
        return overviewVO;
    }

    // ==================== 待办 ====================

    /** 今日待办(TodoDO 各补偿增量 SUM, 钳为正整数) */
    public HomeTodoRes homeTodo(AdminHomeTodoQuery query) {
        query.addSumField("wait_pay_delta", "wait_delivery_delta", "refunding_delta",
                "stock_warn_delta", "sold_out_delta", "wait_verify_delta");

        BizCountMap countMap = realtimeRepo.sum(TodoDO.class, new LambdaQueryWrapper<>(), query);
        HomeTodoRes vo = new HomeTodoRes();
        if (countMap != null) {
            TodoDO todoDO = CollUtil.getFirst(countMap.camelKeyCountMap().toList(TodoDO.class));
            TransferUtils.transfer(vo, todoDO, cn.hutool.core.bean.copier.CopyOptions.create()
                    .setFieldNameEditor(it -> todoFieldToRes(it)));
        }
        return vo;
    }

    // ==================== 近7日趋势 ====================

    /** 近7日 GMV + 上链量趋势(折线图列表): 历史日走日表, 今天走实时表 */
    public List<HomeTrendItemRes> homeTrend(AdminHomeTradeQuery query) {
        DateTime startTime = query.getStartTime();
        DateTime endTime = query.getEndTime() == null ? DateUtil.date() : query.getEndTime();

        List<HomeTrendItemRes> list = new ArrayList<>();
        DateTime day = DateUtil.beginOfDay(startTime);
        while (!day.isAfter(endTime)) {
            AdminHomeTradeQuery dayQuery = new AdminHomeTradeQuery();
            dayQuery.addSumField("amount", "on_chain_count");

            BizCountMap map;
            if (DateUtil.beginOfDay(day).isBefore(DateUtil.beginOfDay(DateUtil.date()))) {
                QueryWrapper<TradeDO> wrapper = new BaseQueryWrapper<TradeDO>()
                        .between("biz_date", day, DateUtil.endOfDay(day));
                map = dayRepo.sum(TradeDO.class, wrapper, dayQuery);
            } else {
                map = realtimeRepo.sum(TradeDO.class, new LambdaQueryWrapper<>(), dayQuery);
            }
            TradeDO tradeDO = map == null ? null : CollUtil.getFirst(map.camelKeyCountMap().toList(TradeDO.class));

            HomeTrendItemRes item = new HomeTrendItemRes();
            item.setDate(DateUtil.format(day, "MM-dd"));
            item.setGmv(tradeDO == null || tradeDO.getAmount() == null ? BigDecimal.ZERO : tradeDO.getAmount());
            item.setOnChainCount(tradeDO == null || tradeDO.getOnChainCount() == null ? 0 : tradeDO.getOnChainCount());
            list.add(item);

            day = day.offset(DateField.DAY_OF_YEAR, 1);
        }
        return list;
    }

    // ==================== 营销渠道收入比例 ====================

    /** 各渠道今日收入(RevenueDO 每渠道字段 SUM), 占比由 Res getter 计算 */
    public HomeChannelRevenueRes homeRevenue(AdminHomeRevenueQuery query) {
        query.addSumField("wx_mini_amount", "oa_amount", "h5_amount",
                "douyin_amount", "app_amount", "pos_amount");

        BizCountMap countMap = realtimeRepo.sum(RevenueDO.class, new LambdaQueryWrapper<>(), query);
        RevenueDO revenueDO = countMap == null ? null : CollUtil.getFirst(countMap.camelKeyCountMap().toList(RevenueDO.class));

        HomeChannelRevenueRes res = new HomeChannelRevenueRes();
        if (revenueDO != null) {
            BigDecimal[] values = {revenueDO.getWxMiniAmount(), revenueDO.getOaAmount(), revenueDO.getH5Amount(),
                    revenueDO.getDouyinAmount(), revenueDO.getAppAmount(), revenueDO.getPosAmount()};
            BigDecimal total = BigDecimal.ZERO;
            for (BigDecimal v : values) {
                if (v != null) {
                    total = total.add(v);
                }
            }
            res.setTotalAmount(total);
            res.setWxMiniAmount(revenueDO.getWxMiniAmount());
            res.setOaAmount(revenueDO.getOaAmount());
            res.setH5Amount(revenueDO.getH5Amount());
            res.setDouyinAmount(revenueDO.getDouyinAmount());
            res.setAppAmount(revenueDO.getAppAmount());
            res.setPosAmount(revenueDO.getPosAmount());
        }
        return res;
    }

    // ==================== 商品中心 ====================

    /** 商品总量统计(GoodsSummaryDO 各字段 SUM) */
    public ProductSummaryRes productSummary(AdminProductQuery query) {
        query.addSumField("spu_count", "sku_count", "on_chain_goods_count", "one_code_bind_count", "today_new_spu_count");
        BizCountMap countMap = realtimeRepo.sum(GoodsSummaryDO.class, new LambdaQueryWrapper<>(), query);
        ProductSummaryRes res = new ProductSummaryRes();
        if (countMap != null) {
            GoodsSummaryDO d = CollUtil.getFirst(countMap.camelKeyCountMap().toList(GoodsSummaryDO.class));
            TransferUtils.transfer(res, d);
        }
        return res;
    }

    /** 商品分类统计(GoodsCategoryDO 按 categoryId 分组) */
    public ProductCategoryRes productCategory(AdminProductQuery query) {
        query.addField("category_id", "count");
        query.addGroupField("category_id");
        BizCountMap countMap = realtimeRepo.sum(GoodsCategoryDO.class, new LambdaQueryWrapper<>(), query);
        ProductCategoryRes res = new ProductCategoryRes();
        List<ProductCategoryRes.CategoryItem> list = new ArrayList<>();
        if (countMap != null) {
            List<GoodsCategoryDO> rows = countMap.camelKeyCountMap().toList(GoodsCategoryDO.class);
            for (GoodsCategoryDO row : rows) {
                ProductCategoryRes.CategoryItem item = new ProductCategoryRes.CategoryItem();
                item.setCategoryId(row.getCategoryId());
                item.setCount(row.getGoodsCount());
                list.add(item);
            }
        }
        res.setCategoryList(list);
        return res;
    }

    /**
     * 品类占比(前5分类 + 其余归"其他")
     *
     * <p>复用 GoodsCategoryDO 分组数据: 按商品数量排序取前5, 剩余分类数量合并为"其他"。
     * 与商品分类(productCategory)的区别: 前者全量分类, 本方法聚合为占比口径。</p>
     */
    public ProductCategoryRes categoryRatio(AdminProductQuery query) {
        query.addField("category_id", "count");
        query.addGroupField("category_id");
        query.initSortField("count", true);
        BizCountMap countMap = realtimeRepo.sum(GoodsCategoryDO.class, new LambdaQueryWrapper<>(), query);
        ProductCategoryRes res = new ProductCategoryRes();
        List<ProductCategoryRes.CategoryItem> list = new ArrayList<>();
        int otherCount = 0;
        if (countMap != null) {
            List<GoodsCategoryDO> rows = countMap.camelKeyCountMap().toList(GoodsCategoryDO.class);
            for (int i = 0; i < rows.size(); i++) {
                GoodsCategoryDO row = rows.get(i);
                if (i < 5) {
                    ProductCategoryRes.CategoryItem item = new ProductCategoryRes.CategoryItem();
                    item.setCategoryId(row.getCategoryId());
                    item.setCount(row.getGoodsCount());
                    list.add(item);
                } else {
                    otherCount += row.getGoodsCount() == null ? 0 : row.getGoodsCount();
                }
            }
        }
        if (otherCount > 0) {
            ProductCategoryRes.CategoryItem other = new ProductCategoryRes.CategoryItem();
            other.setCategoryId(-1L);
            other.setCount(otherCount);
            list.add(other);
        }
        res.setCategoryList(list);
        return res;
    }

    /** 商品状态统计(GoodsStatusDO 各字段 SUM) */
    public ProductStatusRes productStatus(AdminProductQuery query) {
        query.addSumField("on_sale_count", "sold_out_count", "on_shelf_count", "off_shelf_count");
        BizCountMap countMap = realtimeRepo.sum(GoodsStatusDO.class, new LambdaQueryWrapper<>(), query);
        ProductStatusRes res = new ProductStatusRes();
        if (countMap != null) {
            GoodsStatusDO d = CollUtil.getFirst(countMap.camelKeyCountMap().toList(GoodsStatusDO.class));
            TransferUtils.transfer(res, d);
        }
        return res;
    }

    // ==================== 会员中心 ====================

    /** 会员总览(MemberSummaryDO SUM, 复购率 = repurchaseFlag SUM / memberCount) */
    public MemberOverviewRes memberOverview(AdminMemberQuery query) {
        query.addSumField("member_count", "verified_count", "repurchase_flag", "balance_amount");
        BizCountMap countMap = realtimeRepo.sum(MemberSummaryDO.class, new LambdaQueryWrapper<>(), query);
        MemberOverviewRes res = new MemberOverviewRes();
        if (countMap != null) {
            MemberSummaryDO d = CollUtil.getFirst(countMap.camelKeyCountMap().toList(MemberSummaryDO.class));
            res.setMemberCount(d.getMemberCount());
            res.setVerifiedCount(d.getVerifiedCount());
            res.setBalanceAmount(Money.of(d.getBalanceAmount()));
            // 复购率 = 复购数 / 会员总数 * 100
            if (d.getMemberCount() != null && d.getMemberCount() > 0 && d.getRepurchaseFlag() != null) {
                res.setRepurchaseRate(new BigDecimal(d.getRepurchaseFlag())
                        .multiply(new BigDecimal("100"))
                        .divide(new BigDecimal(d.getMemberCount()), 1, java.math.RoundingMode.HALF_UP));
            }
        }
        res.setPointsCount(null); // 待积分表
        return res;
    }

    /** 会员等级分布(MemberLevelDO 按 memberId 明细) */
    public MemberLevelRes memberLevel(AdminMemberQuery query) {
        query.addField("member_id", "level");
        BizCountMap countMap = realtimeRepo.sum(MemberLevelDO.class, new LambdaQueryWrapper<>(), query);
        MemberLevelRes res = new MemberLevelRes();
        List<MemberLevelRes.LevelItem> list = new ArrayList<>();
        if (countMap != null) {
            List<MemberLevelDO> rows = countMap.camelKeyCountMap().toList(MemberLevelDO.class);
            for (MemberLevelDO row : rows) {
                MemberLevelRes.LevelItem item = new MemberLevelRes.LevelItem();
                item.setMemberId(row.getMemberId());
                item.setLevel(row.getLevel());
                list.add(item);
            }
        }
        res.setLevelList(list);
        return res;
    }

    // ==================== 供应商管理 ====================

    /** 供应商总览(实时 SUM + 日表 goods_new_month_count + 动销率代码计算) */
    public SupplierOverviewRes supplierOverview(AdminSupplierQuery query) {
        query.addSumField("supplier_count", "strategic_count", "core_count",
                "goods_on_sale_count", "payable_in_transit");
        BizCountMap countMap = realtimeRepo.sum(SupplierSummaryDO.class, new LambdaQueryWrapper<>(), query);
        SupplierOverviewRes res = new SupplierOverviewRes();
        if (countMap != null) {
            SupplierSummaryDO d = CollUtil.getFirst(countMap.camelKeyCountMap().toList(SupplierSummaryDO.class));
            TransferUtils.transfer(res, d);
        }

        // 本月新增上传: 日表同名字段(当月 1 日至今)
        DateTime now = DateUtil.date();
        DateTime monthStart = DateUtil.beginOfMonth(now);
        AdminSupplierQuery dayQuery = new AdminSupplierQuery();
        dayQuery.addSumField("goods_new_month_count");
        QueryWrapper<SupplierSummaryDO> wrapper = new BaseQueryWrapper<SupplierSummaryDO>()
                .between("biz_date", monthStart, now);
        BizCountMap monthMap = dayRepo.sum(SupplierSummaryDO.class, wrapper, dayQuery);
        if (monthMap != null) {
            SupplierSummaryDO d = CollUtil.getFirst(monthMap.camelKeyCountMap().toList(SupplierSummaryDO.class));
            res.setGoodsNewMonthCount(d.getGoodsNewMonthCount());
        }

        // 30d 动销率: 60d 内商品 id 集合, 出现两次以上且间隔<=30d 的商品数 / 在售商品数
        res.setSellRate30d(calcSellRate30d());
        return res;
    }

    // ==================== 门店管理 ====================

    /** 门店总览(StoreSummaryDO 各字段 SUM) */
    public StoreOverviewRes storeOverview(AdminProductQuery query) {
        query.addSumField("store_count", "pending_audit_count", "multi_store_count",
                "outlet_count", "cert_issued_count", "new_month_count", "county_channel_count");
        BizCountMap countMap = realtimeRepo.sum(StoreSummaryDO.class, new LambdaQueryWrapper<>(), query);
        StoreOverviewRes res = new StoreOverviewRes();
        if (countMap != null) {
            StoreSummaryDO d = CollUtil.getFirst(countMap.camelKeyCountMap().toList(StoreSummaryDO.class));
            TransferUtils.transfer(res, d);
        }
        return res;
    }

    // ==================== 财务结算 ====================

    /** 财务总览(FinanceSummaryDO 各字段 SUM) */
    public FinanceOverviewRes financeOverview(AdminProductQuery query) {
        query.addSumField("month_gmv", "pending_settle_amount", "month_refund_amount", "commission_cost");
        BizCountMap countMap = realtimeRepo.sum(FinanceSummaryDO.class, new LambdaQueryWrapper<>(), query);
        FinanceOverviewRes res = new FinanceOverviewRes();
        if (countMap != null) {
            FinanceSummaryDO d = CollUtil.getFirst(countMap.camelKeyCountMap().toList(FinanceSummaryDO.class));
            TransferUtils.transfer(res, d);
        }
        return res;
    }

    // ==================== 知链存证 ====================

    /** 知链总览(EvidenceSummaryDO 各字段 SUM) */
    public EvidenceOverviewRes evidenceOverview(AdminProductQuery query) {
        query.addSumField("total_evidence_count", "today_on_chain_count", "pending_verify_count",
                "verify_pass_count", "verify_fail_count");
        BizCountMap countMap = realtimeRepo.sum(EvidenceSummaryDO.class, new LambdaQueryWrapper<>(), query);
        EvidenceOverviewRes res = new EvidenceOverviewRes();
        if (countMap != null) {
            EvidenceSummaryDO d = CollUtil.getFirst(countMap.camelKeyCountMap().toList(EvidenceSummaryDO.class));
            TransferUtils.transfer(res, d);
        }
        return res;
    }

    // ==================== 支付中心 ====================

    /** 支付总览(实时 SUM, momRatio/feeRate 计算) */
    public PaymentOverviewRes paymentOverview(AdminPaymentQuery query) {
        query.addSumField("pay_amount", "fee_amount", "split_count", "recon_diff_count");
        BizCountMap countMap = realtimeRepo.sum(PaymentSummaryDO.class, new LambdaQueryWrapper<>(), query);
        PaymentOverviewRes res = new PaymentOverviewRes();
        if (countMap != null) {
            PaymentSummaryDO d = CollUtil.getFirst(countMap.camelKeyCountMap().toList(PaymentSummaryDO.class));
            TransferUtils.transfer(res, d);
            if (d.getPayAmount() != null && d.getPayAmount().compareTo(BigDecimal.ZERO) > 0) {
                // 综合费率 = 手续费/支付额 * 100
                res.setFeeRate(d.getFeeAmount() == null ? BigDecimal.ZERO
                        : d.getFeeAmount().multiply(new BigDecimal("100"))
                          .divide(d.getPayAmount(), 2, java.math.RoundingMode.HALF_UP));
            }
        }
        // momRatio: 需昨日日表对比, 暂 0(待日归档任务)
        res.setMomRatio(BigDecimal.ZERO);
        return res;
    }

    // ==================== 数据洞察 ====================

    /** 月度GMV趋势(按月聚合日表 TradeDO.amount) */
    public InsightMonthGmvRes monthGmv(AdminInsightQuery query) {
        DateTime startTime = query.getStartTime();
        DateTime endTime = query.getEndTime() == null ? DateUtil.date() : query.getEndTime();

        List<String> monthList = new ArrayList<>();
        List<BigDecimal> gmvList = new ArrayList<>();
        DateTime cur = DateUtil.beginOfMonth(startTime);
        while (!cur.isAfter(endTime)) {
            DateTime nextMonth = DateUtil.endOfMonth(cur);
            AdminInsightQuery q = new AdminInsightQuery();
            q.addSumField("amount");
            QueryWrapper<TradeDO> wrapper = new BaseQueryWrapper<TradeDO>()
                    .between("biz_date", DateUtil.beginOfDay(cur), DateUtil.endOfDay(nextMonth));
            BizCountMap map = dayRepo.sum(TradeDO.class, wrapper, q);
            TradeDO d = map == null ? null : CollUtil.getFirst(map.camelKeyCountMap().toList(TradeDO.class));
            monthList.add(DateUtil.format(cur, "yyyy-MM"));
            gmvList.add(d == null || d.getAmount() == null ? BigDecimal.ZERO : d.getAmount());
            cur = nextMonth.offsetNew(DateField.DAY_OF_YEAR, 1);
        }
        InsightMonthGmvRes res = new InsightMonthGmvRes();
        res.setMonthList(monthList);
        res.setGmvList(gmvList);
        return res;
    }

    /** 关联趋势(交易额/订单量/上链量): 复用 CorrelationDO, 不复用 homeTrend */
    public List<HomeTrendItemRes> correlation(AdminInsightQuery query) {
        DateTime startTime = query.getStartTime();
        DateTime endTime = query.getEndTime() == null ? DateUtil.date() : query.getEndTime();

        List<HomeTrendItemRes> list = new ArrayList<>();
        DateTime day = DateUtil.beginOfDay(startTime);
        while (!day.isAfter(endTime)) {
            AdminInsightQuery dayQuery = new AdminInsightQuery();
            dayQuery.addSumField("amount", "order_count", "on_chain_count");

            BizCountMap map;
            if (DateUtil.beginOfDay(day).isBefore(DateUtil.beginOfDay(DateUtil.date()))) {
                QueryWrapper<CorrelationDO> wrapper = new BaseQueryWrapper<CorrelationDO>()
                        .between("biz_date", day, DateUtil.endOfDay(day));
                map = dayRepo.sum(CorrelationDO.class, wrapper, dayQuery);
            } else {
                map = realtimeRepo.sum(CorrelationDO.class, new LambdaQueryWrapper<>(), dayQuery);
            }
            CorrelationDO d = map == null ? null : CollUtil.getFirst(map.camelKeyCountMap().toList(CorrelationDO.class));

            HomeTrendItemRes item = new HomeTrendItemRes();
            item.setDate(DateUtil.format(day, "MM-dd"));
            item.setGmv(d == null || d.getAmount() == null ? BigDecimal.ZERO : d.getAmount());
            item.setOnChainCount(d == null || d.getOnChainCount() == null ? 0 : d.getOnChainCount());
            list.add(item);

            day = day.offset(DateField.DAY_OF_YEAR, 1);
        }
        return list;
    }

    // ==================== 排行 ====================

    /** 商品销售排行(GoodsRankDO 按 goodsId 分组排序) */
    public RankGoodsRes goodsRank(AdminInsightQuery query) {
        query.addSumField("sales_count", "sales_amount");
        query.addGroupField(GoodsRankDO::getGoodsId);
        query.initSortField("sales_amount", true);
        BizCountMap countMap = realtimeRepo.sum(GoodsRankDO.class, new LambdaQueryWrapper<>(), query);
        RankGoodsRes res = new RankGoodsRes();
        List<RankGoodsRes.GoodsRankItem> list = new ArrayList<>();
        if (countMap != null) {
            List<GoodsRankDO> rows = countMap.camelKeyCountMap().toList(GoodsRankDO.class);
            for (GoodsRankDO row : rows) {
                RankGoodsRes.GoodsRankItem item = new RankGoodsRes.GoodsRankItem();
                item.setGoodsId(row.getGoodsId());
                item.setSalesCount(row.getSalesCount());
                item.setSalesAmount(row.getSalesAmount());
                list.add(item);
            }
        }
        res.setGoodsList(list);
        return res;
    }

    /** 店铺成交排行(StoreRankDO 按 storeId 分组排序) */
    public RankStoreRes storeRank(AdminInsightQuery query) {
        query.addField("store_id");
        query.addSumField("sales_amount");
        query.addGroupField("store_id");
        query.initSortField("sales_amount", true);
        BizCountMap countMap = realtimeRepo.sum(StoreRankDO.class, new LambdaQueryWrapper<>(), query);
        RankStoreRes res = new RankStoreRes();
        List<RankStoreRes.StoreRankItem> list = new ArrayList<>();
        if (countMap != null) {
            List<StoreRankDO> rows = countMap.camelKeyCountMap().toList(StoreRankDO.class);
            for (StoreRankDO row : rows) {
                RankStoreRes.StoreRankItem item = new RankStoreRes.StoreRankItem();
                item.setStoreId(row.getStoreId());
                item.setSalesAmount(row.getSalesAmount());
                list.add(item);
            }
        }
        res.setStoreList(list);
        return res;
    }

    // ==================== 私有 ====================

    /** 待办: TodoDO 字段(delta 后缀) → HomeTodoRes 字段(去 delta, count 结尾) */
    private String todoFieldToRes(String field) {
        String noDelta = StrUtil.removeSuffix(field, "_delta");
        return StrUtil.upperFirst(StrUtil.toCamelCase(noDelta)) + "Count";
    }

    /** 30d 动销率: 60d 内商品 id 集合, 去重后出现两次以上(不同日)的商品比例 */
    private BigDecimal calcSellRate30d() {
        // TODO: 从日表 SupplierSummaryDO.goods_id_set(Text) 读取 60d 集合, 代码计算动销率
        // 当前无日归档数据, 返回 null
        return null;
    }
}
