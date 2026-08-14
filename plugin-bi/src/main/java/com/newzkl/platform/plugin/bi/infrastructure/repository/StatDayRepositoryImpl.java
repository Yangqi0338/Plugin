package com.newzkl.platform.plugin.bi.infrastructure.repository;

import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import com.newzkl.platform.plugin.bi.domain.repository.StatDayRepository;
import com.newzkl.platform.plugin.bi.infrastructure.dao.DayWideTableDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.RealtimeWideTableDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.TableNameResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
    public BigDecimal sumField(Class<? extends BIBaseDO> entityClass,
                               LocalDate from, LocalDate to, String fieldName) {
        return dayDAO.sumField(TableNameResolver.dayTable(entityClass), from, to, fieldName);
    }

    @Override
    public int count(Class<? extends BIBaseDO> entityClass, LocalDate from, LocalDate to) {
        return dayDAO.count(TableNameResolver.dayTable(entityClass), from, to);
    }

    @Override
    public List<Map<String, Object>> selectByDateRange(Class<? extends BIBaseDO> entityClass,
                                                       LocalDate from, LocalDate to) {
        return dayDAO.selectByDateRange(TableNameResolver.dayTable(entityClass), from, to);
    }

    @Override
    public void deleteAll(Class<? extends BIBaseDO> entityClass) {
        dayDAO.deleteAll(TableNameResolver.dayTable(entityClass));
    }
}
