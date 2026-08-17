package com.newzkl.platform.plugin.bi.model.enums;

/**
 * BI 事件类型枚举
 *
 * <p>宽表 event_type 字段统一用此枚举, 不再使用裸字符串。
 * 命名 = 业务域前缀 + 动作, 存库用枚举 name。</p>
 */
public enum BiEventType {

    // ==================== 订单 ====================

    /** 订单创建(待付款+1) */
    ORDER_CREATE,
    /** 订单支付成功(GMV+amount, 订单数+1) */
    ORDER_PAY,
    /** 订单售后 */
    ORDER_REFUND,

    // ==================== 库存 / 商品 ====================

    /** 库存变更 */
    INVENTORY_CHANGE,
    /** 商品上架 */
    GOODS_ONLINE,
    /** 商品下架 */
    GOODS_OFFLINE,
    /** 商品审核 */
    GOODS_AUDIT,

    // ==================== 存证(知链) ====================

    /** 上链存证 */
    EVIDENCE_ON_CHAIN,
    /** 存证核验通过 */
    EVIDENCE_VERIFY,
    /** 存证核验失败 */
    EVIDENCE_VERIFY_FAILED,

    // ==================== 供应商 ====================

    /** 供应商入驻 */
    SUPPLIER_ENTER,
    /** 供应商退出 */
    SUPPLIER_EXIT,
    /** 供应商等级升级 */
    SUPPLIER_LEVEL_UP,
    /** 供应商动销 */
    SUPPLIER_SOLD,
    /** 供应商账期结算 */
    SUPPLIER_SETTLE,

    // ==================== 支付 ====================

    /** 支付成功 */
    PAYMENT_PAY,
    /** 手续费 */
    PAYMENT_FEE,
    /** 分账结算 */
    PAYMENT_SPLIT,
    /** 对账差异 */
    PAYMENT_RECON,

    // ==================== 会员 ====================

    /** 会员注册 */
    MEMBER_REGISTER,
    /** 会员注销 */
    MEMBER_CANCEL,
    /** 会员确权 */
    MEMBER_VERIFIED,

    // ==================== 门店 ====================

    /** 门店入驻 */
    STORE_ENTER,
    /** 门店审核 */
    STORE_AUDIT,
    /** 门店证书签发 */
    STORE_CERT_ISSUED
}
