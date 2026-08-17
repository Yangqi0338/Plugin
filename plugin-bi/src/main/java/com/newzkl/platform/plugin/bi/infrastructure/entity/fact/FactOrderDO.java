package com.newzkl.platform.plugin.bi.infrastructure.entity.fact;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.plugin.bi.model.enums.BiEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订单事件事实表(每事件一行, 永久保留)
 *
 * <p>记录"订单支付/退款"事件本身的度量, 是明细真相, 供检查/审计/宽表重建。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class FactOrderDO extends BaseDO {

    /** 订单号
     */
    private Long orderId;

    /** 事件类型(ORDER_PAY/ORDER_REFUND)
     */
    private BiEventType eventType;

    /** 所属端
     */
    private CommonEnum.Client client;

    /** 用户
     */
    private Long userId;

    /** 门店
     */
    private Long storeId;

    /** SPU
     */
    private Long spuId;

    /** 订单状态
     */
    private String status;

    /** 交易额(元)
     */
    private BigDecimal amount;
}
