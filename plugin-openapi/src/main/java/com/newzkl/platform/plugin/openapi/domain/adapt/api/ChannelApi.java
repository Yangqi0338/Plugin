package com.newzkl.platform.plugin.openapi.domain.adapt.api;

import com.newzkl.platform.plugin.openapi.model.command.ChannelSyncCommand;

/**
 * 渠道商跨域出站端口
 *
 * <p>openapi 插件对 biz-account 域渠道商能力的调用收敛于此, 对等旧
 * {@code @DubboReference IChannelFacade}, 将来拆服务时只改本端口实现为远程 consumer,
 * application 层零改动</p>
 *
 * @author KC
 */
public interface ChannelApi {

    /**
     * 同步渠道商基础信息 按调用方账号ID部分更新 未传字段保持原值
     *
     * @param channelId 调用方账号ID 同时是渠道商ID
     * @param command   基础信息同步命令
     */
    void syncBase(Long channelId, ChannelSyncCommand command);
}
