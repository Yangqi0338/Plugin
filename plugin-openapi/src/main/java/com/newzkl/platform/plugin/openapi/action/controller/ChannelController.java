package com.newzkl.platform.plugin.openapi.action.controller;

import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.plugin.openapi.action.cmd.ChannelCmd;
import com.newzkl.platform.plugin.openapi.application.service.IChannelService;
import com.newzkl.platform.plugin.openapi.model.command.ChannelSyncCommand;
import com.newzkl.platform.plugin.openapi.model.constants.Constants;
import com.newzkl.platform.plugin.openapi.model.util.DeveloperContextUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开放平台-渠道商
 *
 * @author KC
 */
@RestController("openApiChannelController")
@RequestMapping("/api/channel")
@Setter(onMethod_ = @Autowired)
@Validated
public class ChannelController {

    private final IChannelService channelService;

    public ChannelController(IChannelService channelService) {
        this.channelService = channelService;
    }

    /**
     * 同步渠道商基础信息
     *
     * <p>入参 {@code license} 为对外契约保留字段, 我方不落库, 转 command 时自然丢弃</p>
     *
     * @param req 基础信息同步入参
     * @return 空结果
     */
    @PostMapping("/syncBase")
    public PlatformResult<Void> syncBase(@Validated @RequestBody ChannelCmd.ChannelBaseSyncReq req) {
        Long channelId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        channelService.syncBase(channelId, TransferUtils.transfer(req, ChannelSyncCommand.class));
        return PlatformResult.success();
    }
}
