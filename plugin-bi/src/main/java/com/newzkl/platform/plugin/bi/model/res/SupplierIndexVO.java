package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 供应商/渠道商首页统计出参
 *
 * <p>迁移自 {@code com.zkl.scm.user.interfaces.controller.model.res.SupplierIndexVO}。</p>
 */
@Data
public class SupplierIndexVO {

    /** 累计订单数 */
    private Integer totalOrderNumber;
    /** 累计订单金额(分) */
    private Integer totalOrderAmount;
    /** 累计退款数 */
    private Integer totalRefundNumber;
    /** 累计退款金额(分) */
    private Integer totalRefundAmount;

    /** 今日订单数 */
    private Integer todayOrderNumber;
    /** 今日退款数 */
    private Integer todayRefundNumber;
    /** 今日订单金额(分) */
    private Integer todayOrderAmount;
    /** 今日退款金额(分) */
    private Integer todayRefundAmount;

    /** 昨日订单数 */
    private Integer yesterdayOrderNumber;
    /** 昨日退款数 */
    private Integer yesterdayRefundNumber;
    /** 昨日订单金额(分) */
    private Integer yesterdayOrderAmount;
    /** 昨日退款金额(分) */
    private Integer yesterdayRefundAmount;

    /** 待付款数 */
    private Integer waitPayNumber;
    /** 待发货数 */
    private Integer waitDeliveryNumber;
    /** 售后单数 */
    private Integer refundPage;

    /** 库存预警 */
    private Integer stockWarn;
    /** 库存空 */
    private Integer stockEmpty;
    /** 待审核数 */
    private Integer waitAudit;

    /** 采购余额(分) */
    private Integer purchaseBalance;
    /** 商品位(分) */
    private Integer goodsSeat;

    /** 今日收入(分) */
    private Integer todayIncomeAmount;
    /** 昨日收入(分) */
    private Integer yesterdayIncomeAmount;

    /** 创建时间 */
    private LocalDateTime createTime;
}