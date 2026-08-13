package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.newzkl.platform.base.common.core.mybatis.entity.BaseIdDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单趋势宽表抽象实体(补偿增量主体)
 *
 * <p><b>不标注 {@code @TableName}</b>, 纯 Java 抽象基类, 不映射任何表。
 * 实时宽表/日宽表共享本主体, 唯一区别是时间维度(日宽表多 bizDate)。</p>
 *
 * <p>字段为"事件对指标的补偿增量"(可为负), SUM 全量增量 = 当前存量:
 * <ul>
 *   <li>下单: waitPayDelta=+1</li>
 *   <li>支付: waitPayDelta=-1, waitDeliveryDelta=+1, amount=GMV, orderCount=1</li>
 *   <li>售后: refundingDelta=+1, refundAmount=退款额</li>
 *   <li>上链: onChainCount=+1</li>
 * </ul></p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class DwsOrderTrend extends BaseIdDO {

    /** 展示维度: 用户 */
    private Long userId;

    /** 展示维度: 租户 */
    private Long clientId;

    /** 展示维度: 门店 */
    private Long storeId;

    // ==================== 流量指标(正增量, SUM=流量) ====================

    /** 度量: 交易额(GMV, 元) */
    private BigDecimal amount;

    /** 度量: 订单数 */
    private Integer orderCount;

    /** 度量: 上链数 */
    private Integer onChainCount;

    /** 度量: 退款额(元) */
    private BigDecimal refundAmount;

    // ==================== 存量指标(补偿增量, SUM=存量) ====================

    /** 增量: 待付款(下单+1, 支付-1) */
    private Integer waitPayDelta;

    /** 增量: 待发货(支付+1, 发货-1) */
    private Integer waitDeliveryDelta;

    /** 增量: 售后中(售后+1, 处理完-1) */
    private Integer refundingDelta;

    /** 事件类型(ORDER_CREATE/ORDER_PAY/ORDER_REFUND/EVIDENCE) */
    private String eventType;

    /** 事件时间 */
    private LocalDateTime eventTime;
}
