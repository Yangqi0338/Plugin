package com.newzkl.platform.plugin.openapi.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.facade.PurseFacade;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.plugin.openapi.domain.adapt.api.FinanceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 财务跨域出站端口实现
 *
 * <p>直调 Base biz-finance {@code PurseFacade}; 入参为元, 由 {@code Money.of(BigDecimal)}
 * 换算为分, 对等旧 {@code IAccountPurseApi.channelSyncByDownStream} 的 {@code amount * 100}</p>
 *
 * @author KC
 */
@Component("openApiFinanceApi")
@RequiredArgsConstructor
public class FinanceApiImpl implements FinanceApi {

    private final PurseFacade purseFacade;

    @Override
    public void channelSyncByDownStream(Long accountId, BigDecimal amount) {
        purseFacade.channelBalanceSync(accountId, amount == null ? null : Money.of(amount));
    }
}
