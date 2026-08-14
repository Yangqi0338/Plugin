package com.newzkl.platform.plugin.bi.domain.constant;

/**
 * BI Redis 缓存 Key 常量
 *
 * <p>所有 BI 缓存 key 统一在此定义, 格式: {@code bi:{module}:{clientId}}</p>
 */
public final class BiRedisKey {

    /** 首页·实时概况 */
    public static final String OVERVIEW = "bi:overview:";

    /** 首页·待办事项 */
    public static final String TODO = "bi:todo:";

    /** 供应商管理总览 */
    public static final String SUPPLIER = "bi:supplier:";

    /** 支付中心总览 */
    public static final String PAYMENT = "bi:payment:";

    /** 商品中心 */
    public static final String PRODUCT = "bi:product:";

    /** 会员中心 */
    public static final String MEMBER = "bi:member:";

    /** 月度GMV趋势 */
    public static final String MONTH_GMV = "bi:monthGmv:";

    /** 品类占比 */
    public static final String CATEGORY = "bi:category:";

    /** 商品排行 */
    public static final String RANK_GOODS = "bi:rank:goods:";

    /** 店铺排行 */
    public static final String RANK_STORE = "bi:rank:store:";

    private BiRedisKey() {}
}
