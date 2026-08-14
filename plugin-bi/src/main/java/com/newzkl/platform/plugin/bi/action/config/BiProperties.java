package com.newzkl.platform.plugin.bi.action.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * BI 全局配置
 *
 * <p>从 application.yml 读取 {@code bi.*} 配置, 支持动态修改。</p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "bi")
public class BiProperties {

    /** 库存紧张阈值(当前库存/总库存 <= 此比例视为紧张), 默认 10% */
    private double stockWarnRatio = 0.10;

    /** 实时概况缓存过期时间(秒) */
    private long overviewCacheTtl = 60;

    /** 待办缓存过期时间(秒) */
    private long todoCacheTtl = 30;

    /** 供应商管理缓存过期时间(秒) */
    private long supplierCacheTtl = 60;

    /** 支付中心缓存过期时间(秒) */
    private long paymentCacheTtl = 60;
}
