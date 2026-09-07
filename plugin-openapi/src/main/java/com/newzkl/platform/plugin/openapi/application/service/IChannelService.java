package com.newzkl.platform.plugin.openapi.application.service;

import com.newzkl.platform.plugin.openapi.model.command.ChannelSyncCommand;

/**
 * 开放平台-渠道商服务
 *
 * @author KC
 */
public interface IChannelService {

    /**
     * 同步渠道商基础信息 按调用方账号ID部分更新 未传字段保持原值
     *
     * @param channelId 调用方账号ID 同时是渠道商ID
     * @param command   基础信息同步命令
     */
    void syncBase(Long channelId, ChannelSyncCommand command);
}
