package com.newzkl.platform.plugin.audit.action.cmd;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import jakarta.validation.constraints.NotNull;

/**
 * 保证金流水提交命令
 *
 * <p>供应商填金额 + 上传支付凭证提交的入参。accountId 与 identity 取登录态, 不入本命令。
 * 金额为正的校验落 domain (Money 非 Number, 不能用 @Positive)</p>
 *
 * @param amount         保证金金额
 * @param promisePayType 保证金类型 (0首次/1补缴/2缓缴)
 * @param payType        支付方式
 * @param certificateUrl 支付凭证
 * @author KC
 */
public record PromiseFlowSubmitCommand(
        @NotNull(message = "金额不能为空") Money amount,
        @NotNull(message = "保证金类型不能为空") PaymentEnum.PromisePayType promisePayType,
        @NotNull(message = "支付方式不能为空") PaymentEnum.PayType payType,
        String certificateUrl) {
}
