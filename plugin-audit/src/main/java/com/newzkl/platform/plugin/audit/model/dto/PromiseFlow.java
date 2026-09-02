package com.newzkl.platform.plugin.audit.model.dto;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 保证金流水
 *
 * <p>保证金审核第二线主数据领域载体, 存 promise_flow 表。审核归属 audit 域</p>
 *
 * @author KC
 */
@Data
public class PromiseFlow {

    /**
     * 保证金流水单主键
     */
    private Long id;
    /**
     * 账号ID
     */
    private Long accountId;
    /**
     * 角色ID
     */
    private AccountEnum.Identity identity;
    /**
     * 保证金类型
     */
    private PaymentEnum.PromisePayType promisePayType;
    /**
     * 金额
     */
    private Money amount;
    /**
     * 支付方式
     */
    private PaymentEnum.PayType payType;
    /**
     * 支付凭证
     */
    private String certificateUrl;
    /**
     * 审核状态
     */
    private AuditEnum.State auditState;
    /**
     * 审核拒绝原因
     */
    private String auditRefuseReason;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
