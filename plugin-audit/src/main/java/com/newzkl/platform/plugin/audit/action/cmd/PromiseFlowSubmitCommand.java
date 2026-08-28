package com.newzkl.platform.plugin.audit.action.cmd;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import jakarta.validation.constraints.Positive;

/**
 * 保证金流水提交命令
 *
 * <p>供应商填金额 + 上传支付凭证提交的入参。accountId 取登录态, 不入本命令</p>
 *
 * @param amount         保证金金额
 * @param promisePayType 保证金类型 (0首次/1补缴/2缓缴)
 * @param payType        支付方式
 * @param certificateUrl 支付凭证
 * @author KC
 */
public record PromiseFlowSubmitCommand(
        @Positive(message = "金额必须大于0") Money amount,
        PaymentEnum.PromisePayType promisePayType,
        PaymentEnum.PayType payType,
        String certificateUrl) {
}
