package com.newzkl.platform.plugin.bi.infrastructure.entity.fact;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.plugin.bi.domain.constant.BiEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 支付事件事实表(每事件一行)
 *
 * <p>支付中心支付总额/手续费/分账/对账差异 统计的数据源。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FactPaymentDO extends BaseDO {

    /** 支付单/分账单ID
     */
    private Long paymentId;

    /** 事件类型(支付/手续费/分账/对账)
     */
    private BiEventType eventType;

    /** 所属端
     */
    private CommonEnum.Client client;

    /** 关联订单
     */
    private Long orderId;

    /** 支付金额(元)
     */
    private BigDecimal payAmount;

    /** 手续费(元)
     */
    private BigDecimal feeAmount;
}
