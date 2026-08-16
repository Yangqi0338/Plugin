package com.newzkl.platform.plugin.bi.domain.repository;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;

/**
 * 实时宽表仓储
 *
 * <p>表维度: 所有实时宽表(dws_realtime_*) 的通用操作。
 * 字段通过 {@link SFunction} 引用实体 getter, 不传字符串列名。</p>
 */
public interface StatRealtimeRepository {

    /** 插入一条记录(动态列) */
    void insert(BIBaseDO entity);

    /** SUM(指定字段), 通用所有数值字段 */
    <T extends BIBaseDO, R> Money sumField(Class<T> entityClass, SFunction<T, R> field);
    
    <T extends BIBaseDO> BizCountMap sum(Class<T> entityClass,
                                            AbstractWrapper<?, ?, ?> queryWrapper,
                                            QuerySupport querySupport);
    
    /** 查询全部行(供归档) */
    java.util.List<java.util.Map<String, Object>> selectAll(Class<? extends BIBaseDO> entityClass);

    /** COUNT(*) */
    int count(Class<? extends BIBaseDO> entityClass);

    /** 清空表 */
    void deleteAll(Class<? extends BIBaseDO> entityClass);
}
