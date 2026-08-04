package com.newzkl.platform.plugin.audit.adapter;

import com.newzkl.platform.plugin.audit.port.ChannelRelationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 渠道关系适配器
 *
 * <p>Base 当前无 spuChannelRelation 查询能力, 降级返回空列表加告警日志, 工单通知因此不发渠道, 待渠道关系基建补齐</p>
 *
 * @author KC
 */
@Slf4j
@Component("auditPluginChannelRelationAdapter")
public class ChannelRelationAdapter implements ChannelRelationPort {

    @Override
    public List<Long> spuChannelRelation(Long spuId) {
        log.warn("TODO[infra-gap]: Base 缺 spuChannelRelation 查询, spuId={} 渠道关系降级返回空列表", spuId);
        return List.of();
    }
}
