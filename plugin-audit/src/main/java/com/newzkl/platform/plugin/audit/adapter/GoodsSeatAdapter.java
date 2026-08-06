package com.newzkl.platform.plugin.audit.adapter;

import com.newzkl.platform.base.biz.finance.facade.GoodsSeatFacade;
import com.newzkl.platform.plugin.audit.port.GoodsSeatPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 商品位适配器
 *
 * <p>转调biz-finance GoodsSeatFacade扣减商品位</p>
 *
 * @author KC
 */
@Component("auditPluginGoodsSeatAdapter")
@RequiredArgsConstructor
public class GoodsSeatAdapter implements GoodsSeatPort {

    private final GoodsSeatFacade goodsSeatFacade;

    @Override
    public void supplierSubmitSubGoodsSeat(Long supplierId, Long spuId) {
        goodsSeatFacade.supplierSubmitSubGoodsSeat(supplierId, spuId);
    }
}
