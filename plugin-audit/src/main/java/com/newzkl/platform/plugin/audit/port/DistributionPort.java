package com.newzkl.platform.plugin.audit.port;

import java.util.List;

/**
 * 分销铺货出站端口
 *
 * <p>SPU 上下架时触发分销铺货事件, 实现落 adapter 层转调 biz-market DistributionDomain</p>
 *
 * @author KC
 */
public interface DistributionPort {

    /**
     * 触发上下架铺货事件
     *
     * @param state 目标状态
     * @param spuIdList SPU 主键列表
     * @param needUpdate 是否需要更新 1 是 0 否
     */
    void upDownEvent(Integer state, List<Long> spuIdList, Integer needUpdate);
}
