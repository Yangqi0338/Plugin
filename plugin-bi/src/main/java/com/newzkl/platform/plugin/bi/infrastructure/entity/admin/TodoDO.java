package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 首页·待办事项宽表(状态补偿增量, SUM=当前存量)
 *
 * <p>字段 = 待办查询 6 项展示字段。事件落库时计算:
 * 库存紧张/售罄在库存变更事件落库时当场按 stockWarnRatio 判断写入(不查事实表)。
 * 实时表: dws_realtime_admin_todo_trend | 日表: dws_day_admin_todo_trend</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = AccountEnum.Client.ADMIN)
public class TodoDO extends BIBaseDO {

    /** 等待付款(下单+1, 支付-1)
     */
    private Integer waitPayDelta;

    /** 等待发货(支付+1, 发货-1)
     */
    private Integer waitDeliveryDelta;

    /** 售后中(售后+1, 处理完-1)
     */
    private Integer refundingDelta;

    /** 库存紧张(变紧张+1, 解除-1)
     */
    private Integer stockWarnDelta;

    /** 商品售罄(变售罄+1, 补货-1)
     */
    private Integer soldOutDelta;

    /** 待核验存证(上链+1, 核验完成-1)
     */
    private Integer waitVerifyDelta;
}
