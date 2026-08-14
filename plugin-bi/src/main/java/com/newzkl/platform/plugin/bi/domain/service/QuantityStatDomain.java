package com.newzkl.platform.plugin.bi.domain.service;

import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.TradeDO;
import com.newzkl.platform.plugin.bi.domain.repository.StatDayRepository;
import com.newzkl.platform.plugin.bi.domain.repository.StatRealtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 数量统计领域服务
 *
 * <p>计数类指标的统计计算, 含订单数/上链数/供应商数/会员数等。
 * sumField 返回 BigDecimal, 此处转 int 输出。</p>
 */
@Service
@RequiredArgsConstructor
public class QuantityStatDomain {

    private static final String FIELD_ORDER_COUNT = "order_count";
    private static final String FIELD_ON_CHAIN_COUNT = "on_chain_count";

    private final StatRealtimeRepository realtimeRepo;
    private final StatDayRepository dayRepo;

    /** 今日实时订单数 */
    public int todayOrderCount() {
        return realtimeRepo.sumField(TradeDO.class, FIELD_ORDER_COUNT).intValue();
    }

    /** 今日实时上链数 */
    public int todayOnChainCount() {
        return realtimeRepo.sumField(TradeDO.class, FIELD_ON_CHAIN_COUNT).intValue();
    }

    /** 历史某日订单数 */
    public int dayOrderCount(LocalDate date) {
        return dayRepo.sumField(TradeDO.class, date, date, FIELD_ORDER_COUNT).intValue();
    }

    /** 近7日订单量趋势(前6天日表 + 今天实时) */
    public List<Integer> weekOrderCountTrend() {
        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(6);
        List<Integer> result = new ArrayList<>();
        LocalDate cur = from;
        while (cur.isBefore(today)) {
            result.add(dayRepo.sumField(TradeDO.class, cur, cur, FIELD_ORDER_COUNT).intValue());
            cur = cur.plusDays(1);
        }
        result.add(realtimeRepo.sumField(TradeDO.class, FIELD_ORDER_COUNT).intValue());
        return result;
    }

    /** 任意实体数值字段聚合(实时表, 返回 int) */
    public int sumRealtimeField(Class<? extends BIBaseDO> entityClass, String fieldName) {
        BigDecimal v = realtimeRepo.sumField(entityClass, fieldName);
        return v == null ? 0 : v.intValue();
    }

    /** 任意实体数值字段聚合(日表, 日期范围, 返回 int) */
    public int sumDayField(Class<? extends BIBaseDO> entityClass,
                           LocalDate from, LocalDate to, String fieldName) {
        BigDecimal v = dayRepo.sumField(entityClass, from, to, fieldName);
        return v == null ? 0 : v.intValue();
    }
}
