package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseIdDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单事件事实表(每事件一行, 永久保留)
 *
 * <p>记录"订单完成/支付"这个消费事件本身的度量, 是明细真相, 供检查/审计/宽表重建。
 * 字段语义由业务事件决定(bi 需要交易额 → 存 amount)。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("fact_order")
public class FactOrder extends BaseIdDO {

    /** 事件业务ID(订单号) */
    private Long orderId;

    /** 事件类型(PAY/COMPLETE/REFUND) */
    private String eventType;

    /** 租户 */
    private Long clientId;

    /** 用户 */
    private Long userId;

    /** 门店 */
    private Long storeId;

    /** SPU */
    private Long spuId;

    /** 订单状态(完成/关闭/售后) */
    private String status;

    /** 交易额(元) — bi 需要的数据 */
    private BigDecimal amount;

    /** 事件时间 */
    private LocalDateTime eventTime;
}
