package com.newzkl.platform.plugin.bi.domain.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.TradeDO;
import com.newzkl.platform.plugin.bi.domain.repository.StatDayRepository;
import com.newzkl.platform.plugin.bi.domain.repository.StatRealtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 价格统计领域服务
 *
 * <p>金额/价格类指标的统计计算, 含 GMV/退款/手续费/应付等。
 * 统一走 sumField 按字段名聚合, 不区分具体实体。</p>
 */
@Service
@RequiredArgsConstructor
public class PriceStatDomain {

    /** 交易额字段(GMV) */
    private static final String FIELD_AMOUNT = "amount";

    private final StatRealtimeRepository realtimeRepo;
    private final StatDayRepository dayRepo;

    /** 今日实时 GMV(近7日趋势宽表) */
    public BigDecimal todayGmv() {
        return realtimeRepo.sumField(TradeDO.class, FIELD_AMOUNT);
    }

    /** 历史某日 GMV(从日表) */
    public BigDecimal dayGmv(LocalDate date) {
        return dayRepo.sumField(TradeDO.class, date, date, FIELD_AMOUNT);
    }

    /** 近7日 GMV 趋势 */
    public Map<String, BigDecimal> homeGmvTrend(List<DateTime> dateTimeList) {
        LocalDate today = DateUtil.date();
        LocalDate from = today.minusDays(6);
        Map<String, BigDecimal> result = new HashMap<>();
        LocalDate cur = from;
        while (cur.isBefore(today)) {
            result.put(dayRepo.sumField(TradeDO.class, cur, cur, FIELD_AMOUNT));
            cur = cur.plusDays(1);
        }
        result.add(realtimeRepo.sumField(TradeDO.class, FIELD_AMOUNT));
        return result;
    }

    /** 本月 GMV(从日表) */
    public BigDecimal monthGmv() {
        LocalDate now = LocalDate.now();
        return dayRepo.sumField(TradeDO.class, now.withDayOfMonth(1), now, FIELD_AMOUNT);
    }

    /** 任意实体数值字段聚合(实时表) */
    public BigDecimal sumRealtimeField(Class<? extends BIBaseDO> entityClass, String fieldName) {
        return realtimeRepo.sumField(entityClass, fieldName);
    }

    /** 任意实体数值字段聚合(日表, 日期范围) */
    public BigDecimal sumDayField(Class<? extends BIBaseDO> entityClass,
                                  LocalDate from, LocalDate to, String fieldName) {
        return dayRepo.sumField(entityClass, from, to, fieldName);
    }
}
