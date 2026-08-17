package com.newzkl.platform.plugin.bi.infrastructure.dao;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.mybatis.MybatisPlusConfig;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;

/**
 * 宽表名解析器
 *
 * <p>表名 = {@code dws_{realtime|day}_{client.code}_{entitySnake}},
 * {@code entitySnake} 复用 {@link MybatisPlusConfig#buildTableName} 的表名处理规则
 * (驼峰转下划线 + 去 DO/_d_o 后缀), 不在此复写。</p>
 */
public final class TableNameResolver {

    private TableNameResolver() {}

    /** 获取实时表名 */
    public static String realtimeTable(Class<? extends BIBaseDO> entityClass) {
        return "dws_realtime_" + clientCode(entityClass) + "_" + entitySnake(entityClass);
    }

    /** 获取日表名 */
    public static String dayTable(Class<? extends BIBaseDO> entityClass) {
        return "dws_day_" + clientCode(entityClass) + "_" + entitySnake(entityClass);
    }

    private static String clientCode(Class<? extends BIBaseDO> entityClass) {
        BITableName ann = entityClass.getAnnotation(BITableName.class);
        if (ann == null) {
            throw new IllegalArgumentException(entityClass.getSimpleName() + " 缺少 @BITableName 注解");
        }
        return ann.client().getCode();
    }

    /** 表名主体: 驼峰转下划线后走 MybatisPlusConfig 去 DO 后缀(不在此复写规则)
     */
    private static String entitySnake(Class<?> entityClass) {
        // OverviewDO -> overview_d_o -> MybatisPlusConfig 去掉 _d_o -> overview
        String snake = StrUtil.toUnderlineCase(entityClass.getSimpleName());
        return MybatisPlusConfig.buildTableName(snake);
    }
}
