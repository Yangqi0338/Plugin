package com.newzkl.platform.plugin.bi.infrastructure.entity.fact;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.plugin.bi.model.enums.BiEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 退款事件事实表(每事件一行)
 *
 * <p>财务结算本月退款金额的数据源。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FactRefundDO extends BaseDO {

    /** 订单ID
     */
    private Long orderId;

    /** 事件类型
     */
    private BiEventType eventType;

    /** 所属端
     */
    private CommonEnum.Client client;

    /** 退款金额(元)
     */
    private BigDecimal amount;
}
