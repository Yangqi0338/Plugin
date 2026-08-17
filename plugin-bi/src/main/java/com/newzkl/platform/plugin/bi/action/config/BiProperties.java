package com.newzkl.platform.plugin.bi.action.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * BI 全局配置属性
 *
 * <p>前缀 {@code bi}。字段为 {@code public static}, 供各层直接引用
 * (如 {@code BiProperties.stockWarnRatio}), Spring 经 setter 绑定静态变量。</p>
 */
@Configuration
@ConfigurationProperties(prefix = "bi")
public class BiProperties {

    /**
     * 库存紧张阈值
     * @ext 当前库存/总库存 <= 此比例视为紧张, 默认 10%
     */
    public static double stockWarnRatio = 0.10;

    /**
     * 实时概况缓存过期时间
     * @ext 秒
     */
    public static long overviewCacheTtl = 60;

    /**
     * 待办缓存过期时间
     * @ext 秒
     */
    public static long todoCacheTtl = 30;

    /**
     * 商品中心缓存过期时间
     * @ext 秒
     */
    public static long productCacheTtl = 60;

    /**
     * 会员中心缓存过期时间
     * @ext 秒
     */
    public static long memberCacheTtl = 60;

    /**
     * 门店管理缓存过期时间
     * @ext 秒
     */
    public static long storeCacheTtl = 60;

    /**
     * 财务结算缓存过期时间
     * @ext 秒
     */
    public static long financeCacheTtl = 60;

    /**
     * 知链存证缓存过期时间
     * @ext 秒
     */
    public static long evidenceCacheTtl = 60;

    /**
     * 供应商管理缓存过期时间
     * @ext 秒
     */
    public static long supplierCacheTtl = 60;

    /**
     * 支付中心缓存过期时间
     * @ext 秒
     */
    public static long paymentCacheTtl = 60;

    public void setStockWarnRatio(double stockWarnRatio) {
        BiProperties.stockWarnRatio = stockWarnRatio;
    }
    public void setOverviewCacheTtl(long overviewCacheTtl) {
        BiProperties.overviewCacheTtl = overviewCacheTtl;
    }
    public void setTodoCacheTtl(long todoCacheTtl) {
        BiProperties.todoCacheTtl = todoCacheTtl;
    }
    public void setProductCacheTtl(long productCacheTtl) {
        BiProperties.productCacheTtl = productCacheTtl;
    }
    public void setMemberCacheTtl(long memberCacheTtl) {
        BiProperties.memberCacheTtl = memberCacheTtl;
    }
    public void setStoreCacheTtl(long storeCacheTtl) {
        BiProperties.storeCacheTtl = storeCacheTtl;
    }
    public void setFinanceCacheTtl(long financeCacheTtl) {
        BiProperties.financeCacheTtl = financeCacheTtl;
    }
    public void setEvidenceCacheTtl(long evidenceCacheTtl) {
        BiProperties.evidenceCacheTtl = evidenceCacheTtl;
    }
    public void setSupplierCacheTtl(long supplierCacheTtl) {
        BiProperties.supplierCacheTtl = supplierCacheTtl;
    }
    public void setPaymentCacheTtl(long paymentCacheTtl) {
        BiProperties.paymentCacheTtl = paymentCacheTtl;
    }
}
