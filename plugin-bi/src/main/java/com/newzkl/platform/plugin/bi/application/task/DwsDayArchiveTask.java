package com.newzkl.platform.plugin.bi.application.task;

import cn.hutool.core.bean.BeanUtil;
import com.newzkl.platform.base.common.core.redis.aspect.DistributedLock;
import com.newzkl.platform.plugin.bi.domain.adapt.repository.StatDayRepository;
import com.newzkl.platform.plugin.bi.domain.adapt.repository.StatRealtimeRepository;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.CorrelationDO;
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
import com.newzkl.platform.plugin.bi.infrastructure.entity.channel.ChannelHomeDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.channel.ChannelWeekTradeDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.partner.PartnerHomeDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.supplier.SupplierHomeDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.supplier.SupplierSettleTrendDO;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * BI 日宽表归档任务
 *
 * <p>每日 00:30 执行: 把实时宽表(当日补偿增量)复制进日宽表(bizDate=昨天), 然后清空实时宽表。
 * 日宽表与实时宽表共享主体(补偿增量), 区别仅时间维度(biz_date)。</p>
 *
 * <p>宽表清单随 {@code @BITableName} 实体新增而扩充, 每个实时/日宽表各归档一次。</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DwsDayArchiveTask {
    
    private final StatRealtimeRepository realtimeRepo;
    private final StatDayRepository dayRepo;

    @XxlJob("biDayArchive")
    @DistributedLock
    public void archive() {
        LocalDate bizDate = LocalDate.now().minusDays(1);
        for (Class<? extends BIBaseDO> entityClass : WIDE_TABLES) {
            try {
                archiveOne(entityClass, bizDate);
            } catch (Exception e) {
                log.error("[BI] 归档失败: {}", entityClass.getSimpleName(), e);
            }
        }
    }
    
    /** 全部 admin 宽表实体(实时/日共享结构) */
    private static final List<Class<? extends BIBaseDO>> WIDE_TABLES = List.of(
            OverviewDO.class,
            TradeDO.class,
            TodoDO.class,
            RevenueDO.class,
            GoodsSummaryDO.class,
            GoodsStatusDO.class,
            GoodsCategoryDO.class,
            MemberSummaryDO.class,
            MemberLevelDO.class,
            SupplierSummaryDO.class,
            PaymentSummaryDO.class,
            GoodsRankDO.class,
            StoreRankDO.class,
            CorrelationDO.class,
            SupplierHomeDO.class,
            SupplierSettleTrendDO.class,
            ChannelHomeDO.class,
            ChannelWeekTradeDO.class,
            PartnerHomeDO.class
    );
    
    /** 单个实体归档 */
    private void archiveOne(Class<? extends BIBaseDO> entityClass, LocalDate bizDate) {
        List<Map<String, Object>> rows = realtimeRepo.selectAll(entityClass);
        if (rows.isEmpty()) {
            log.info("[BI] 归档: {} 实时宽表为空, 跳过", entityClass.getSimpleName());
            return;
        }
        
        // 逐行复制到日表
        List<BIBaseDO> entityList = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            BIBaseDO entity = BeanUtil.toBean(row, entityClass);
            entityList.add(entity);
        }
        for (BIBaseDO entity : entityList) {
            dayRepo.insert(entity, bizDate);
        }
        
        // 清空实时宽表
        realtimeRepo.deleteAll(entityClass);
        log.info("[BI] 归档完成: {} 行数={}, bizDate={}", entityClass.getSimpleName(), rows.size(), bizDate);
    }
}
