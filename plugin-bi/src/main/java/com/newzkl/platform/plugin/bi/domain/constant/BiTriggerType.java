package com.newzkl.platform.plugin.bi.domain.constant;

/**
 * BI 模拟触发事件类型
 *
 * <p>开发/联调阶段模拟业务事件源, 生产真实事件接入后废弃。</p>
 */
public enum BiTriggerType {

    /** 会员注册 */
    MEMBER_REGISTER,

    /** 会员注销 */
    MEMBER_CANCEL,

    /** 上链存证 */
    EVIDENCE_ON_CHAIN,

    /** 存证核验 */
    EVIDENCE_VERIFY,

    /** 库存变更 */
    INVENTORY_CHANGE,

    /** 商品上架 */
    GOODS_ON_SHELF,

    /** 商品下架 */
    GOODS_OFF_SHELF,

    /** 商品审核 */
    GOODS_AUDIT,

    /** 门店入驻 */
    STORE_ENTER,

    /** 订单创建 */
    ORDER_CREATE,

    /** 订单支付(回查金额) */
    ORDER_PAY
}
