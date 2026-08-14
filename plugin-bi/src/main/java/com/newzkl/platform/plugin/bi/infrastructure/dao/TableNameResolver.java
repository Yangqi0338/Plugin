package com.newzkl.platform.plugin.bi.infrastructure.dao;

import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;

/**
 * 宽表名解析器
 *
 * <p>根据 {@link BITableName} 注解和实体类名, 解析实时表/日表的表名。</p>
 */
public final class TableNameResolver {

    private TableNameResolver() {}

    /** 获取实时表名: dws_realtime_{client}_{entitySnake} */
    public static String realtimeTable(Class<? extends BIBaseDO> entityClass) {
        BITableName ann = entityClass.getAnnotation(BITableName.class);
        if (ann == null) {
            throw new IllegalArgumentException(entityClass.getSimpleName() + " 缺少 @BITableName 注解");
        }
        return "dws_realtime_" + ann.client().getCode() + "_" + entitySnake(entityClass, ann);
    }

    /** 获取日表名: dws_day_{client}_{entitySnake} */
    public static String dayTable(Class<? extends BIBaseDO> entityClass) {
        BITableName ann = entityClass.getAnnotation(BITableName.class);
        if (ann == null) {
            throw new IllegalArgumentException(entityClass.getSimpleName() + " 缺少 @BITableName 注解");
        }
        return "dws_day_" + ann.client().getCode() + "_" + entitySnake(entityClass, ann);
    }

    /** 实体名蛇形小写: 去掉 Trend 后缀(如有) */
    private static String entitySnake(Class<?> entityClass, BITableName ann) {
        if (!ann.suffix().isEmpty()) {
            return ann.suffix();
        }
        String name = entityClass.getSimpleName();
        if (name.endsWith("Trend")) {
            name = name.substring(0, name.length() - 5);
        }
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
