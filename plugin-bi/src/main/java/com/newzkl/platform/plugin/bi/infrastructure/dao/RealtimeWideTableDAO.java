package com.newzkl.platform.plugin.bi.infrastructure.dao;

import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 实时宽表 DAO(MyBatis + XML)
 *
 * <p>使用 {@code BiRealtimeMapper} 命名空间, 表名由 ${_tableName} 动态传入,
 * insert 按实体非 null 字段动态拼接列, 不硬编码字段超集。</p>
 */
@Repository
public interface RealtimeWideTableDAO {
    
    /** 动态列插入 */
     void insert(String tableName, Set<String> columns, Collection<Object> values);

    /** SUM(指定字段), 通用所有数值字段 */
    BigDecimal sumField(String tableName, String fieldName);

    int count(String tableName);

    void deleteAll(String tableName);

    /**
     * 实体转列名 Map(蛇形列名 -> 非 null 值)
     * 反射收集基类 + 子类所有 getter, 过滤 id/class/null。
     */
    public static Map<String, Object> entityColumns(BIBaseDO entity) {
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
                    map.put(camelToSnake(name), val);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("BIBaseDO 字段反射失败", e);
        }
        return map;
    }

    /** 驼峰转蛇形: orderCount -> order_count */
    public static String camelToSnake(String name) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                if (sb.length() > 0) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
