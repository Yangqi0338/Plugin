package com.newzkl.platform.plugin.openapi.application.service.impl;

import com.newzkl.platform.plugin.openapi.application.service.IChannelService;
import com.newzkl.platform.plugin.openapi.domain.adapt.api.ChannelApi;
import com.newzkl.platform.plugin.openapi.model.command.ChannelSyncCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 开放平台-渠道商服务实现
 *
 * @author KC
 */
@Service("openApiChannelService")
@RequiredArgsConstructor
public class ChannelServiceImpl implements IChannelService {

    private final ChannelApi channelApi;

    @Override
    public void syncBase(Long channelId, ChannelSyncCommand command) {
        channelApi.syncBase(channelId, command);
    }
}
