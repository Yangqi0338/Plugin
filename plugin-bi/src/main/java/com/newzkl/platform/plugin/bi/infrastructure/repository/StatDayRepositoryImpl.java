package com.newzkl.platform.plugin.bi.infrastructure.repository;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import com.newzkl.platform.plugin.bi.domain.repository.StatDayRepository;
import com.newzkl.platform.plugin.bi.infrastructure.dao.DayWideTableDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.RealtimeWideTableDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.TableNameResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 日宽表仓储实现
 */
@Repository
@RequiredArgsConstructor
public class StatDayRepositoryImpl implements StatDayRepository {

    private final DayWideTableDAO dayDAO;

    @Override
    public void insert(BIBaseDO entity, LocalDate bizDate) {
        Map<String, Object> cols = RealtimeWideTableDAO.entityColumns(entity);
        dayDAO.insert(TableNameResolver.dayTable(entity.getClass()), cols.keySet(), cols.values(), bizDate);
    }

    @Override
    public <T extends BIBaseDO, R> Money sumField(Class<T> entityClass, DateTime from, DateTime to,
                                                  SFunction<T, R> field) {
        return dayDAO.sumField(TableNameResolver.dayTable(entityClass), from, to,
                StatRealtimeRepositoryImpl.columnOf(field));
    }

    @Override
    public int count(Class<? extends BIBaseDO> entityClass, DateTime from, DateTime to) {
        return dayDAO.count(TableNameResolver.dayTable(entityClass), from, to);
    }

    @Override
    public List<Map<String, Object>> selectByDateRange(Class<? extends BIBaseDO> entityClass, DateTime from, DateTime to) {
        return dayDAO.selectByDateRange(TableNameResolver.dayTable(entityClass), from, to);
    }

    @Override
    public void deleteAll(Class<? extends BIBaseDO> entityClass) {
        dayDAO.deleteAll(TableNameResolver.dayTable(entityClass));
    }
    
    @Override
    public <T extends BIBaseDO> BizCountMap sum(Class<T> entityClass, AbstractWrapper<?, ?, ?> queryWrapper, QuerySupport querySupport) {
        BizCountMap countMap = dayDAO.sumMapOne(TableNameResolver.dayTable(entityClass), queryWrapper, querySupport);
        return countMap;
    }
}
