package com.newzkl.platform.plugin.audit.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.AuditBaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 保证金流水
 *
 * <p>保证金审核第二线主数据: 供应商线下打款填金额、传凭证提交, 平台人工审核。
 * 审核态直接落本表, 审核归属 audit 域, 故实体落 plugin-audit</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class PromiseFlowDO extends AuditBaseDO {

    /**
     * 账号ID (查询)
     */
    @Index
    private Long accountId;
    /**
     * 角色ID
     */
    @Index
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
     * 审核状态 (查询)
     */
    private AuditEnum.State auditState;
    /**
     * 审核拒绝原因
     */
    private String auditRefuseReason;
}
