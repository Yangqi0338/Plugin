package com.newzkl.platform.plugin.audit.adapter;

import com.newzkl.platform.base.biz.market.domain.distribution.DistributionDomain;
import com.newzkl.platform.base.biz.market.model.event.distribution.WorkTableUpDownEventMq;
import com.newzkl.platform.base.biz.market.model.req.distribution.DistributionsBatchUpdateReq;
import com.newzkl.platform.plugin.audit.port.DistributionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分销铺货适配器
 *
 * <p>转调 biz-market DistributionDomain: 先 upDownEvent 组批量更新请求, 再 batchUpdateDistributions 落铺货状态</p>
 *
 * @author KC
 */
@Component("auditPluginDistributionAdapter")
@RequiredArgsConstructor
public class DistributionAdapter implements DistributionPort {

    private final DistributionDomain distributionDomain;

    @Override
    public void upDownEvent(Integer state, List<Long> spuIdList, Integer needUpdate) {
        WorkTableUpDownEventMq event = new WorkTableUpDownEventMq();
        event.setEnable(state);
        event.setSpuIdList(spuIdList);
        event.setNeedUpdate(needUpdate);
        DistributionsBatchUpdateReq req = distributionDomain.upDownEvent(event);
        distributionDomain.batchUpdateDistributions(req);
    }
}
