package com.newzkl.platform.plugin.bi.infrastructure.config;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.mybatis.MybatisPlusConfig;
import com.newzkl.platform.plugin.bi.infrastructure.dao.TableNameResolver;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * BI 宽表双表 DDL 生成器
 *
 * <p>autotable 仅识别 {@code @TableName}/mpe {@code @Table}, 无法识别自定义 {@link BITableName}。
 * 本类启动时扫描全部 {@code @BITableName} 实体, 按 {@link TableNameResolver} 表名规则
 * 生成并执行两张表:
 * <ul>
 *   <li>实时表: {@code dws_realtime_{client}_{entity}} — 当日增量, 无 biz_date</li>
 *   <li>日表:   {@code dws_day_{client}_{entity}} — T+1 归档, 含 {@code biz_date DATE}</li>
 * </ul>
 * 字段类型映射与 Base {@code AutoTableAdapter} 一致(BigDecimal/枚举/Money 等)。</p>
 *
 * <p>仅非生产 profile 兜底执行(生产由 DBA 脚本); 幂等: {@code CREATE TABLE IF NOT EXISTS}。</p>
 */
@Slf4j
@Component
@Profile("!prod")
@RequiredArgsConstructor
public class BiTableAutoDdl implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    /** 扫描的包根: 全部宽表实体所在包 */
    private static final String ENTITY_PACKAGE = "com.newzkl.platform.plugin.bi.infrastructure.entity";

    @Override
    public void run(ApplicationArguments args) {
        List<Class<? extends BIBaseDO>> entities = scanBiEntities();
        if (entities.isEmpty()) {
            log.info("[BI] 未扫描到 @BITableName 宽表实体, 跳过 DDL 生成");
            return;
        }
        for (Class<? extends BIBaseDO> entityClass : entities) {
            try {
                String realtimeTable = TableNameResolver.realtimeTable(entityClass);
                String dayTable = TableNameResolver.dayTable(entityClass);
                Map<String, String> columns = buildColumns(entityClass);

                jdbcTemplate.execute(buildCreateSql(realtimeTable, columns, false));
                jdbcTemplate.execute(buildCreateSql(dayTable, columns, true));
                log.info("[BI] 宽表 DDL 就绪: {} (实时), {} (日, 含 biz_date)", realtimeTable, dayTable);
            } catch (Exception e) {
                log.error("[BI] 宽表 DDL 生成失败: {}", entityClass.getSimpleName(), e);
            }
        }
    }

    /** 扫描所有带 @BITableName 注解且继承 BIBaseDO 的实体 */
    private List<Class<? extends BIBaseDO>> scanBiEntities() {
        List<Class<? extends BIBaseDO>> result = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(BITableName.class));
        for (BeanDefinition bd : scanner.findCandidateComponents(ENTITY_PACKAGE)) {
            try {
                Class<?> clazz = Class.forName(bd.getBeanClassName());
                if (BIBaseDO.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(BITableName.class)) {
                    result.add((Class<? extends BIBaseDO>) clazz);
                }
            } catch (ClassNotFoundException e) {
                log.warn("[BI] 扫描类加载失败: {}", bd.getBeanClassName(), e);
            }
        }
        return result;
    }

    /** 收集继承链所有字段: 列名 -> 类型定义(蛇形命名) */
    private Map<String, String> buildColumns(Class<?> entityClass) {
        Map<String, String> columns = new LinkedHashMap<>();
        Class<?> cur = entityClass;
        while (cur != null && cur != Object.class) {
            for (Field field : cur.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                String column = camelToUnderline(field.getName());
                if (!columns.containsKey(column)) {
                    columns.put(column, columnType(field.getType()));
                }
            }
            cur = cur.getSuperclass();
        }
        return columns;
    }

    /** 类型映射(对齐 Base AutoTableAdapter 规则) */
    private String columnType(Class<?> type) {
        if (type == Long.class || type == long.class) {
            return "BIGINT";
        }
        if (type == Integer.class || type == int.class) {
            return "INT";
        }
        if (type == BigDecimal.class) {
            return "DECIMAL(20,2)";
        }
        if (type == String.class) {
            return "VARCHAR(255)";
        }
        if (type == LocalDateTime.class) {
            return "DATETIME";
        }
        if (type == LocalDate.class) {
            return "DATE";
        }
        if (type == Boolean.class || type == boolean.class) {
            return "TINYINT(1)";
        }
        if (type.isEnum()) {
            return "VARCHAR(64)";
        }
        // 其余(如 Money 等)兜底 VARCHAR
        return "VARCHAR(255)";
    }

    /** 拼 CREATE TABLE: day 表额外 biz_date 列 */
    private String buildCreateSql(String tableName, Map<String, String> columns, boolean isDay) {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS `").append(tableName).append("` (");
        sb.append("`id` BIGINT NOT NULL COMMENT '主键', ");
        for (Map.Entry<String, String> e : columns.entrySet()) {
            if ("id".equals(e.getKey())) {
                continue;
            }
            sb.append('`').append(e.getKey()).append("` ").append(e.getValue()).append(", ");
        }
        if (isDay) {
            sb.append("`biz_date` DATE NOT NULL COMMENT '业务日期', ");
        }
        sb.append("PRIMARY KEY (`id`)");
        if (isDay) {
            sb.append(", KEY `idx_biz_date` (`biz_date`)");
        }
        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BI 宽表'");
        return sb.toString();
    }

    private String camelToUnderline(String name) {
        String snake = StrUtil.toUnderlineCase(name);
        return MybatisPlusConfig.buildTableName(snake);
    }
}
