package com.newzkl.platform.plugin.bi.infrastructure.repository;

import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import com.newzkl.platform.plugin.bi.domain.repository.StatRealtimeRepository;
import com.newzkl.platform.plugin.bi.infrastructure.dao.RealtimeWideTableDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.TableNameResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 实时宽表仓储实现
 */
@Repository
@RequiredArgsConstructor
public class StatRealtimeRepositoryImpl implements StatRealtimeRepository {

    private final RealtimeWideTableDAO realtimeDAO;

    @Override
    public void insert(BIBaseDO entity) {
        Map<String, Object> cols = RealtimeWideTableDAO.entityColumns(entity);
        realtimeDAO.insert(TableNameResolver.dayTable(entity.getClass()), cols.keySet(), cols.values());
    }

    @Override
    public BigDecimal sumField(Class<? extends BIBaseDO> entityClass, String fieldName) {
        return realtimeDAO.sumField(TableNameResolver.realtimeTable(entityClass), fieldName);
    }

    @Override
    public int count(Class<? extends BIBaseDO> entityClass) {
        return realtimeDAO.count(TableNameResolver.realtimeTable(entityClass));
    }

    @Override
    public void deleteAll(Class<? extends BIBaseDO> entityClass) {
        realtimeDAO.deleteAll(TableNameResolver.realtimeTable(entityClass));
    }
}
