package com.newzkl.platform.plugin.bi.infrastructure.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import com.newzkl.platform.plugin.bi.domain.repository.StatRealtimeRepository;
import com.newzkl.platform.plugin.bi.infrastructure.dao.RealtimeWideTableDAO;
import com.newzkl.platform.plugin.bi.infrastructure.dao.TableNameResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
        realtimeDAO.insert(TableNameResolver.realtimeTable(entity.getClass()), cols.keySet(), cols.values());
    }

    @Override
    public <T extends BIBaseDO, R> Money sumField(Class<T> entityClass, SFunction<T, R> field) {
        return realtimeDAO.sumField(TableNameResolver.realtimeTable(entityClass), columnOf(field));
    }
    
    @Override
    public <T extends BIBaseDO> BizCountMap sum(Class<T> entityClass,
                                                   AbstractWrapper<?, ?, ?> queryWrapper,
                                                   QuerySupport querySupport) {
        BizCountMap countMap = realtimeDAO.sumMapOne(TableNameResolver.realtimeTable(entityClass), queryWrapper, querySupport);
        return countMap;
    }

    @Override
    public java.util.List<java.util.Map<String, Object>> selectAll(Class<? extends BIBaseDO> entityClass) {
        return realtimeDAO.selectAll(TableNameResolver.realtimeTable(entityClass));
    }

    @Override
    public int count(Class<? extends BIBaseDO> entityClass) {
        return realtimeDAO.count(TableNameResolver.realtimeTable(entityClass));
    }

    @Override
    public void deleteAll(Class<? extends BIBaseDO> entityClass) {
        realtimeDAO.deleteAll(TableNameResolver.realtimeTable(entityClass));
    }

    /** SFunction getter 引用 → 蛇形列名(MyBatis-Plus 内置能力) */
    static String columnOf(SFunction<?, ?> field) {
        LambdaMeta meta = LambdaUtils.extract(field);
        String method = meta.getImplMethodName();
        String prop = StrUtil.removePrefix(method, "get");
        prop = StrUtil.lowerFirst(prop);
        return StringUtils.camelToUnderline(prop);
    }
}
