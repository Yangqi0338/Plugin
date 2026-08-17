package com.newzkl.platform.plugin.bi.infrastructure.entity.fact;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.plugin.bi.model.enums.BiEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 结算事件事实表(每事件一行)
 *
 * <p>财务结算待结算金额/分销佣金支出的数据源。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FactSettleDO extends BaseDO {

    /** 结算单号
     */
    private String settleNo;

    /** 事件类型
     */
    private BiEventType eventType;

    /** 所属端
     */
    private CommonEnum.Client client;

    /** 金额(元)
     */
    private BigDecimal amount;

    /** 结算类型(待结算/佣金)
     */
    private String settleType;
}
