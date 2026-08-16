package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 首页·近7日交易与上链趋势宽表(流量指标)
 *
 * <p>字段 = 趋势查询三曲线。按事件日落库, 查历史走日表(bizDate), 查今天走实时表。
 * 实时表: dws_realtime_admin_trade_trend | 日表: dws_day_admin_trade_trend</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN)
public class TradeDO extends BIBaseDO {

    /** 交易额(GMV, 元)
     */
    private BigDecimal amount;

    /** 上链量
     */
    private Integer onChainCount;
}
