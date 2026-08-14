package com.newzkl.platform.plugin.bi.domain.repository;

import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 日宽表仓储
 *
 * <p>表维度: 所有日宽表(dws_day_*) 的通用操作。</p>
 */
public interface StatDayRepository {

    /** 插入一条记录(含 bizDate, 动态列) */
    void insert(BIBaseDO entity, LocalDate bizDate);

    /** SUM(指定字段) 按日期范围 */
    BigDecimal sumField(Class<? extends BIBaseDO> entityClass,
                        LocalDate from, LocalDate to, String fieldName);

    /** COUNT(*) 按日期范围 */
    int count(Class<? extends BIBaseDO> entityClass, LocalDate from, LocalDate to);

    /** 按日期范围查询(原始行) */
    List<Map<String, Object>> selectByDateRange(Class<? extends BIBaseDO> entityClass,
                                                LocalDate from, LocalDate to);

    /** 清空表 */
    void deleteAll(Class<? extends BIBaseDO> entityClass);
}
