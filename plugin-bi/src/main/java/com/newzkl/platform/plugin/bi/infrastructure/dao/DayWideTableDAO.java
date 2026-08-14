package com.newzkl.platform.plugin.bi.infrastructure.dao;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 日宽表 DAO(MyBatis + XML)
 *
 * <p>使用 {@code BiDayMapper} 命名空间, 表名由 ${_tableName} 动态传入,
 * biz_date 固定前置, 其余字段按实体非 null 动态拼接。</p>
 */
@Repository
public interface DayWideTableDAO {
    /** 动态列插入(biz_date 固定前置) */
    void insert(String tableName, Set<String> columns, Collection<Object> values, LocalDate bizDate);

    /** SUM(指定字段) 按日期范围 */
    BigDecimal sumField(String tableName, LocalDate from, LocalDate to, String fieldName);

    int count(String tableName, LocalDate from, LocalDate to);

    List<Map<String, Object>> selectByDateRange(String tableName, LocalDate from, LocalDate to);

    void deleteAll(String tableName);
}