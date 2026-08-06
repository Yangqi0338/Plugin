package com.newzkl.platform.plugin.audit.adapter;

import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.plugin.audit.port.SpuSubmitPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * SPU提交适配器
 *
 * <p>转调biz-goods SpuDomain.spuSubmit回写提交状态</p>
 *
 * @author KC
 */
@Component("auditPluginSpuSubmitAdapter")
@RequiredArgsConstructor
public class SpuSubmitAdapter implements SpuSubmitPort {

    private final SpuDomain spuDomain;

    @Override
    public void spuSubmit(Long spuId, Long flowId) {
        spuDomain.spuSubmit(spuId, flowId);
    }
}
