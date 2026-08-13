package com.newzkl.platform.plugin.bi.application.service;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.plugin.bi.infrastructure.dao.DwsDayOrderTrendDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.DwsRealtimeOrderTrendDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.FactBlockchainDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.FactGoodsDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.FactInventoryDAO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.DwsDayOrderTrend;
import com.newzkl.platform.plugin.bi.infrastructure.entity.DwsRealtimeOrderTrend;
import com.newzkl.platform.plugin.bi.infrastructure.entity.FactBlockchain;
import com.newzkl.platform.plugin.bi.infrastructure.entity.FactGoods;
import com.newzkl.platform.plugin.bi.infrastructure.entity.FactInventory;
import com.newzkl.platform.plugin.bi.model.res.HomeOverviewVO;
import com.newzkl.platform.plugin.bi.model.res.HomeTodoVO;
import com.newzkl.platform.plugin.bi.model.res.HomeTrendVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页工作台统计服务
 *
 * <p>数据源分层:
 * <ul>
 *   <li>实时概况: 实时宽表 SUM(当日流量) + Redis 写时删缓存</li>
 *   <li>待办事项: 日宽表按状态聚合(存量) + 事实表 COUNT(库存/商品/区块链)</li>
 *   <li>近7日趋势: 日宽表(历史6天) + 实时宽表(今天) 拼接</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomeBiService {

    private final DwsRealtimeOrderTrendDAO realtimeOrderTrendDAO;
    private final DwsDayOrderTrendDAO dayOrderTrendDAO;
    private final FactInventoryDAO factInventoryDAO;
    private final FactGoodsDAO factGoodsDAO;
    private final FactBlockchainDAO factBlockchainDAO;

    /** 库存紧张阈值(剩余≤10% 紧张) */
    private static final BigDecimal STOCK_WARN_RATIO = new BigDecimal("0.10");

    // ==================== 实时概况 ====================

    /**
     * 实时概况: 今日流量(SUM 实时宽表), Redis 写时删读
     */
    public HomeOverviewVO overview() {
        Long clientId = currentClientId();
        String cacheKey = "bi:overview:" + (clientId == null ? "global" : clientId);

        HomeOverviewVO cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        // SUM 实时宽表当日流量
        LambdaQueryWrapper<DwsRealtimeOrderTrend> qw = new LambdaQueryWrapper<>();
        if (clientId != null) {
            qw.eq(DwsRealtimeOrderTrend::getClientId, clientId);
        }
        List<DwsRealtimeOrderTrend> rows = realtimeOrderTrendDAO.selectList(qw);

        BigDecimal todayGmv = BigDecimal.ZERO;
        int orderCount = 0;
        int onChainCount = 0;
        int waitPay = 0;
        int waitDelivery = 0;
        int refunding = 0;
        for (DwsRealtimeOrderTrend row : rows) {
            todayGmv = todayGmv.add(row.getAmount() == null ? BigDecimal.ZERO : row.getAmount());
            orderCount += row.getOrderCount() == null ? 0 : row.getOrderCount();
            onChainCount += row.getOnChainCount() == null ? 0 : row.getOnChainCount();
            waitPay += row.getWaitPayDelta() == null ? 0 : row.getWaitPayDelta();
            waitDelivery += row.getWaitDeliveryDelta() == null ? 0 : row.getWaitDeliveryDelta();
            refunding += row.getRefundingDelta() == null ? 0 : row.getRefundingDelta();
        }

        HomeOverviewVO vo = new HomeOverviewVO();
        vo.setTodayGmv(todayGmv);
        vo.setTodayOrderCount(orderCount);
        vo.setTodayPayOrderCount(orderCount);
        vo.setTodayEvidenceCount(onChainCount);
        vo.setTodayVerifyCount(onChainCount);
        vo.setTodayMemberCount(0); // 会员事实表未接
        vo.setMonthGmv(BigDecimal.ZERO); // 月累计走日宽表, 后续补
        vo.setMonthOrderCount(0);
        vo.setStockWarnCount(countStockWarn(clientId));
        vo.setWaitPayCount(Math.max(waitPay, 0));
        vo.setWaitDeliveryCount(Math.max(waitDelivery, 0));
        vo.setRefundingCount(Math.max(refunding, 0));

        RedisUtil.set(cacheKey, vo, 60, java.util.concurrent.TimeUnit.SECONDS);
        return vo;
    }

    // ==================== 待办事项 ====================

    /**
     * 待办事项: 日宽表按状态聚合 + 事实表 COUNT
     */
    public HomeTodoVO todo() {
        Long clientId = currentClientId();
        HomeTodoVO vo = new HomeTodoVO();

        // 1. 日宽表全量增量(历史累计)
        LambdaQueryWrapper<DwsDayOrderTrend> dayQw = new LambdaQueryWrapper<>();
        if (clientId != null) {
            dayQw.eq(DwsDayOrderTrend::getClientId, clientId);
        }
        List<DwsDayOrderTrend> dayRows = dayOrderTrendDAO.selectList(dayQw);

        // 2. 实时宽表今天增量
        LambdaQueryWrapper<DwsRealtimeOrderTrend> rtQw = new LambdaQueryWrapper<>();
        if (clientId != null) {
            rtQw.eq(DwsRealtimeOrderTrend::getClientId, clientId);
        }
        List<DwsRealtimeOrderTrend> rtRows = realtimeOrderTrendDAO.selectList(rtQw);

        // 3. 存量 = 日宽表增量 SUM + 今天增量 SUM(补偿增量求和 = 当前存量)
        int waitPay = 0, waitDelivery = 0, refunding = 0;
        for (DwsDayOrderTrend row : dayRows) {
            waitPay += row.getWaitPayDelta() == null ? 0 : row.getWaitPayDelta();
            waitDelivery += row.getWaitDeliveryDelta() == null ? 0 : row.getWaitDeliveryDelta();
            refunding += row.getRefundingDelta() == null ? 0 : row.getRefundingDelta();
        }
        for (DwsRealtimeOrderTrend row : rtRows) {
            waitPay += row.getWaitPayDelta() == null ? 0 : row.getWaitPayDelta();
            waitDelivery += row.getWaitDeliveryDelta() == null ? 0 : row.getWaitDeliveryDelta();
            refunding += row.getRefundingDelta() == null ? 0 : row.getRefundingDelta();
        }

        vo.setWaitPayCount(Math.max(waitPay, 0));
        vo.setWaitDeliveryCount(Math.max(waitDelivery, 0));
        vo.setWaitReceiveCount(0);
        vo.setRefundingCount(Math.max(refunding, 0));

        // 事实表: 库存紧张/售罄, 待审核商品, 待核验存证
        vo.setStockWarnCount(countStockWarn(clientId));
        vo.setWaitAuditGoodsCount(countWaitAuditGoods(clientId));
        vo.setWaitAuditMerchantCount(0);
        vo.setWaitVerifyCount(countWaitVerify(clientId));

        return vo;
    }

    // ==================== 近7日交易趋势 ====================

    /**
     * 近7日交易趋势: 日宽表(历史6天) + 实时宽表(今天)
     */
    public HomeTrendVO trend() {
        Long clientId = currentClientId();
        LocalDate today = LocalDate.now();

        List<String> dimensionList = new ArrayList<>();
        List<Integer> orderCountList = new ArrayList<>();
        List<BigDecimal> gmvList = new ArrayList<>();

        // 前6天: 查日宽表
        for (int i = 6; i >= 1; i--) {
            LocalDate d = today.minusDays(i);
            dimensionList.add(d.toString());
            LambdaQueryWrapper<DwsDayOrderTrend> qw = new LambdaQueryWrapper<>();
            qw.eq(DwsDayOrderTrend::getBizDate, d);
            if (clientId != null) {
                qw.eq(DwsDayOrderTrend::getClientId, clientId);
            }
            List<DwsDayOrderTrend> rows = dayOrderTrendDAO.selectList(qw);
            int orderCount = 0;
            BigDecimal gmv = BigDecimal.ZERO;
            for (DwsDayOrderTrend row : rows) {
                orderCount += row.getOrderCount() == null ? 0 : row.getOrderCount();
                gmv = gmv.add(row.getAmount() == null ? BigDecimal.ZERO : row.getAmount());
            }
            orderCountList.add(orderCount);
            gmvList.add(gmv);
        }

        // 今天: 实时宽表 SUM
        dimensionList.add(today.toString());
        LambdaQueryWrapper<DwsRealtimeOrderTrend> qw = new LambdaQueryWrapper<>();
        if (clientId != null) {
            qw.eq(DwsRealtimeOrderTrend::getClientId, clientId);
        }
        List<DwsRealtimeOrderTrend> rows = realtimeOrderTrendDAO.selectList(qw);
        int todayOrder = 0;
        BigDecimal todayGmv = BigDecimal.ZERO;
        for (DwsRealtimeOrderTrend row : rows) {
            todayOrder += row.getOrderCount() == null ? 0 : row.getOrderCount();
            todayGmv = todayGmv.add(row.getAmount() == null ? BigDecimal.ZERO : row.getAmount());
        }
        orderCountList.add(todayOrder);
        gmvList.add(todayGmv);

        HomeTrendVO vo = new HomeTrendVO();
        vo.setDimensionList(dimensionList);
        vo.setOrderCountList(orderCountList);
        vo.setGmvList(gmvList);
        return vo;
    }

    // ==================== 私有 ====================

    private Long currentClientId() {
        // 当前登录租户, 后续从 SecurityUtils 取; 现在返回 null(全局)
        return null;
    }

    /**
     * 库存紧张/售罄: 从库存事实表取最新值判断(剩余≤10% 紧张, =0 售罄)
     */
    private int countStockWarn(Long clientId) {
        try {
            LambdaQueryWrapper<FactInventory> qw = new LambdaQueryWrapper<>();
            if (clientId != null) {
                qw.eq(FactInventory::getClientId, clientId);
            }
            qw.orderByDesc(FactInventory::getEventTime);
            List<FactInventory> all = factInventoryDAO.selectList(qw);
            // 按 sku 取最新一条
            java.util.Map<Long, FactInventory> latest = new java.util.HashMap<>();
            for (FactInventory f : all) {
                latest.putIfAbsent(f.getSkuId(), f);
            }
            int warn = 0;
            for (FactInventory f : latest.values()) {
                Integer total = f.getTotalStock();
                Integer cur = f.getCurrentStock();
                if (cur == null || cur == 0) continue; // 售罄单独算
                if (total != null && total > 0
                        && BigDecimal.valueOf(cur).compareTo(BigDecimal.valueOf(total).multiply(STOCK_WARN_RATIO)) <= 0) {
                    warn++;
                }
            }
            return warn;
        } catch (Exception e) {
            log.warn("库存紧张统计失败", e);
            return 0;
        }
    }

    private int countWaitAuditGoods(Long clientId) {
        try {
            LambdaQueryWrapper<FactGoods> qw = new LambdaQueryWrapper<>();
            qw.eq(FactGoods::getStatus, "WAIT_AUDIT");
            if (clientId != null) {
                qw.eq(FactGoods::getClientId, clientId);
            }
            return Math.toIntExact(factGoodsDAO.selectCount(qw));
        } catch (Exception e) {
            log.warn("待审核商品统计失败", e);
            return 0;
        }
    }

    private int countWaitVerify(Long clientId) {
        try {
            LambdaQueryWrapper<FactBlockchain> qw = new LambdaQueryWrapper<>();
            qw.eq(FactBlockchain::getStatus, "PENDING_CONFIRM");
            if (clientId != null) {
                qw.eq(FactBlockchain::getClientId, clientId);
            }
            return Math.toIntExact(factBlockchainDAO.selectCount(qw));
        } catch (Exception e) {
            log.warn("待核验存证统计失败", e);
            return 0;
        }
    }
}
