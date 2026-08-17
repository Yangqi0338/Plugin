package com.newzkl.platform.plugin.bi.model.annotation;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BI 宽表表名注解
 *
 * <p>标注在宽表实体类上, AutoTable 识别后自动生成两张表:
 * <ul>
 *   <li>{@code dws_realtime_{client}_{tableName}} — 实时宽表(当日, 无 bizDate)</li>
 *   <li>{@code dws_day_{client}_{tableName}} — 日宽表(T+1, 自动增加 bizDate 字段)</li>
 * </ul>
 * 其中 {@code tableName} = 实体类名经 {@link com.newzkl.platform.base.common.core.mybatis.MybatisPlusConfig#buildTableName}
 * 处理(去 DO/_d_o 后缀), {@code client} 取枚举 code 拼接表名。</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BITableName {

    /** 统计所属端(平台/供应商/渠道商等), 由 {@link CommonEnum.Client} 决定宽表归属 */
    CommonEnum.Client client();
}
