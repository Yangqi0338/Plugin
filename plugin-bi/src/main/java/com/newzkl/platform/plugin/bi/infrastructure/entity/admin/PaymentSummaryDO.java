package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 支付中心总览宽表
 * @ext 沿用实时表逻辑, 字段无 today 前缀
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = AccountEnum.Client.ADMIN)
public class PaymentSummaryDO extends BIBaseDO {

    /** 支付总额(元) */
    private BigDecimal payAmount;

    /** 手续费支出(元) */
    private BigDecimal feeAmount;

    /** 分账结算(笔) */
    private Integer splitCount;

    /** 对账差异(笔) */
    private Integer reconDiffCount;
}
