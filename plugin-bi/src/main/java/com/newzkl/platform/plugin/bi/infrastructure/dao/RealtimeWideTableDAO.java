package com.newzkl.platform.plugin.bi.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import org.apache.ibatis.annotations.Param;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 实时宽表通用 DAO(MyBatis + XML)
 *
 * <p>表名由调用方传入(经 {@link TableNameResolver} 解析), insert 按实体非 null 字段动态拼接列。
 * 不包含租户/端的查询条件 —— client 是宽表归属端(由 @BITableName 决定), 不是租户过滤条件。</p>
 */
@Repository
public interface RealtimeWideTableDAO {

    /** 动态列插入 */
    void insert(String tableName, Set<String> columns, Collection<Object> values);

    /** SUM(指定字段), 通用所有数值字段 */
    Money sumField(String tableName, String fieldName);
    
    BizCountMap sumMapOne(String tableName, @Param(Constants.WRAPPER) AbstractWrapper<?, ?, ?> wrapper, @Param("query") QuerySupport querySupport);

    /** 行数 */
    int count(String tableName);

    /** 查询全部行(无条件, 供归档) */
    List<Map<String, Object>> selectAll(String tableName);

    /** 清空表 */
    void deleteAll(String tableName);

    /**
     * 实体转列名 Map(列名 -> 非 null 值)
     * 列名用 MyBatis-Plus 内置 camelToUnderline 转换, 过滤 id/class。
     */
    static Map<String, Object> entityColumns(BIBaseDO entity) {
        Map<String, Object> map = new LinkedHashMap<>();
        try {
            java.beans.BeanInfo bi = java.beans.Introspector.getBeanInfo(entity.getClass());
            for (java.beans.PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                String name = pd.getName();
                if ("class".equals(name) || "id".equals(name)) {
                    continue;
                }
                Object val = pd.getReadMethod().invoke(entity);
                if (val != null) {
                    map.put(StringUtils.camelToUnderline(name), val);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("BIBaseDO 字段反射失败", e);
        }
        return map;
    }
}
