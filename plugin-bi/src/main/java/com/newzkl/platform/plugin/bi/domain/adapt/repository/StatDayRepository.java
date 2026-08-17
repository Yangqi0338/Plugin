package com.newzkl.platform.plugin.bi.domain.adapt.repository;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;

import java.util.List;
import java.util.Map;

/**
 * 日宽表仓储
 *
 * <p>表维度: 所有日宽表(dws_day_*) 的通用操作。
 * 字段通过 {@link SFunction} 引用实体 getter, 不传字符串列名。</p>
 */
public interface StatDayRepository {

    /** 插入一条记录(含 bizDate, 动态列) */
    void insert(BIBaseDO entity, java.time.LocalDate bizDate);

    /** SUM(指定字段) 按日期范围 */
    <T extends BIBaseDO, R> Money sumField(Class<T> entityClass, DateTime from, DateTime to, SFunction<T, R> field);

    /** COUNT(*) 按日期范围 */
    int count(Class<? extends BIBaseDO> entityClass, DateTime from, DateTime to);

    /** 按日期范围查询(返回 map 行, 便于渲染) */
    List<Map<String, Object>> selectByDateRange(Class<? extends BIBaseDO> entityClass, DateTime from, DateTime to);

    /** 清空表 */
    void deleteAll(Class<? extends BIBaseDO> entityClass);
    
    <T extends BIBaseDO> BizCountMap sum(Class<T> entityClass,
                                            AbstractWrapper<?, ?, ?> queryWrapper,
                                            QuerySupport querySupport);
}
