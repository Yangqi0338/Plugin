package com.newzkl.platform.plugin.audit.port;

import java.util.List;

/**
 * 渠道关系出站端口
 *
 * <p>查 SPU 关联的渠道主键, 用于工单通知, Base 当前无 spuChannelRelation, adapter 降级返回空列表</p>
 *
 * @author KC
 */
public interface ChannelRelationPort {

    /**
     * 查 SPU 关联的渠道主键
     *
     * @param spuId SPU 主键
     * @return 渠道主键列表, 无关联时返回空列表
     */
    List<Long> spuChannelRelation(Long spuId);
}
