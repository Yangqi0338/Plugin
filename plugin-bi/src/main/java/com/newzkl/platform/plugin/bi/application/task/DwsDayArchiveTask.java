package com.newzkl.platform.plugin.bi.application.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newzkl.platform.base.common.core.redis.aspect.DistributedLock;
import com.newzkl.platform.plugin.bi.infrastructure.dao.DwsDayOrderTrendDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.DwsRealtimeOrderTrendDAO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.DwsDayOrderTrend;
import com.newzkl.platform.plugin.bi.infrastructure.entity.DwsRealtimeOrderTrend;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 日宽表存档任务
 *
 * <p>每日 00:30 执行: 把实时宽表(当日补偿增量)复制进日宽表(加 bizDate=昨天),
 * 然后清空实时宽表。日宽表与实时宽表共享主体(补偿增量), 区别仅时间维度。</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DwsDayArchiveTask {

    private final DwsRealtimeOrderTrendDAO realtimeOrderTrendDAO;
    private final DwsDayOrderTrendDAO dayOrderTrendDAO;

    @XxlJob("biDayArchive")
    @DistributedLock(key = "'biDayArchive'")
    public void archive() {
        // 归档昨天的数据(实时宽表此时存的还是昨天 00:30 前的内容, 在 00:30 执行时实时表是"昨天"的)
        // 注意: 若 00:30 执行, 实时宽表从 00:00 开始累计的是今天; 这里按"当前日期-1"归档更稳
        // 实现: 把当前实时宽表全部行, 复制到日宽表并标记 bizDate=昨天; 再清空实时宽表
        LocalDate bizDate = LocalDate.now().minusDays(1);

        List<DwsRealtimeOrderTrend> rows = realtimeOrderTrendDAO.selectList(new LambdaQueryWrapper<>());
        if (rows.isEmpty()) {
            log.info("[BI] 日宽表存档: 实时宽表为空, 跳过");
            return;
        }

        for (DwsRealtimeOrderTrend row : rows) {
            DwsDayOrderTrend day = new DwsDayOrderTrend();
            day.setBizDate(bizDate);
            day.setUserId(row.getUserId());
            day.setClientId(row.getClientId());
            day.setStoreId(row.getStoreId());
            day.setAmount(row.getAmount());
            day.setOrderCount(row.getOrderCount());
            day.setOnChainCount(row.getOnChainCount());
            day.setRefundAmount(row.getRefundAmount());
            day.setWaitPayDelta(row.getWaitPayDelta());
            day.setWaitDeliveryDelta(row.getWaitDeliveryDelta());
            day.setRefundingDelta(row.getRefundingDelta());
            day.setEventType(row.getEventType());
            day.setEventTime(row.getEventTime());
            dayOrderTrendDAO.insert(day);
        }

        // 清空实时宽表
        realtimeOrderTrendDAO.delete(new LambdaQueryWrapper<>());
        log.info("[BI] 日宽表存档完成: bizDate={}, 行数={}", bizDate, rows.size());
    }
}
