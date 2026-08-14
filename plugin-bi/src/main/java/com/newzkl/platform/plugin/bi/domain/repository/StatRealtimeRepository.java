package com.newzkl.platform.plugin.bi.domain.repository;

import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;

import java.math.BigDecimal;

/**
 * 实时宽表仓储
 *
 * <p>表维度: 所有实时宽表(dws_realtime_*) 的通用操作。</p>
 */
public interface StatRealtimeRepository {

    /** 插入一条记录(动态列) */
    void insert(BIBaseDO entity);

    /** SUM(指定字段), 通用所有数值字段 */
    BigDecimal sumField(Class<? extends BIBaseDO> entityClass, String fieldName);

    /** COUNT(*) */
    int count(Class<? extends BIBaseDO> entityClass);

    /** 清空表 */
    void deleteAll(Class<? extends BIBaseDO> entityClass);
}
