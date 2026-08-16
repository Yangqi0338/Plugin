package com.newzkl.platform.plugin.bi.infrastructure.dao;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 日宽表通用 DAO(MyBatis + XML)
 *
 * <p>表名由调用方传入(经 {@link TableNameResolver} 解析), biz_date 固定前置,
 * 其余字段按实体非 null 动态拼接。时间统一用 Hutool {@link DateTime}。</p>
 */
@Repository
public interface DayWideTableDAO {

    /** 动态列插入(biz_date 固定前置) */
    void insert(String tableName, Set<String> columns, Collection<Object> values, LocalDate bizDate);

    /** SUM(指定字段) 按日期范围 */
    Money sumField(String tableName, DateTime from, DateTime to, String fieldName);

    /** 行数(日期范围) */
    int count(String tableName, DateTime from, DateTime to);

    /** 按日期范围查询(返回 map 行, 便于渲染) */
    List<Map<String, Object>> selectByDateRange(String tableName, DateTime from, DateTime to);

    /** 清空表 */
    void deleteAll(String tableName);
    
    BizCountMap sumMapOne(String tableName, @Param(Constants.WRAPPER) AbstractWrapper<?, ?, ?> wrapper, @Param("query") QuerySupport querySupport);
}
