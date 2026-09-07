package com.newzkl.platform.plugin.openapi.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.model.req.ChannelReq;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.plugin.openapi.domain.adapt.api.ChannelApi;
import com.newzkl.platform.plugin.openapi.model.command.ChannelSyncCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 渠道商跨域出站端口实现
 *
 * <p>直调 Base biz-account {@code ChannelClientDomain.channelEdit}, 落到
 * {@code updateById} 时 mybatis-plus 默认 {@code FieldStrategy.NOT_NULL},
 * 未传字段不进 SET 子句, 故部分更新不会清空 name/state/auditState 等未传列</p>
 *
 * @author KC
 */
@Component("openApiChannelApi")
@RequiredArgsConstructor
public class ChannelApiImpl implements ChannelApi {

    private final ChannelClientDomain channelClientDomain;

    @Override
    public void syncBase(Long channelId, ChannelSyncCommand command) {
        ChannelReq channelReq = TransferUtils.transfer(command, ChannelReq.class);
        channelReq.setId(channelId);
        channelClientDomain.channelEdit(channelReq);
    }
}
