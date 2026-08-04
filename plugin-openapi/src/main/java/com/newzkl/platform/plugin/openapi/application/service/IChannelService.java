package com.newzkl.platform.plugin.openapi.application.service;

import com.zkl.scm.rpc.model.ApiPage;
import com.zkl.scm.rpc.user.ThirdUseStoreCdkRes;
import com.zkl.scm.user.rpc.model.channel.ChannelCdkUseReq;
import com.zkl.scm.user.rpc.model.channel.ChannelCodeSyncReq;
import com.zkl.scm.user.rpc.model.channel.ChannelOptionSyncReq;
import com.zkl.scm.user.rpc.model.role.CdkApiQueryReq;
import com.zkl.scm.user.rpc.model.role.CdkVO;

/**
 * @author muc_fang
 * @Description: 渠道商交互业务
 * @date 2023/12/159:57
 */
public interface IChannelService {

    void syncCode(ChannelCodeSyncReq channelCodeSyncReq);

    void syncOption(Long accountId, ChannelOptionSyncReq req);

    ThirdUseStoreCdkRes useCdk(ChannelCdkUseReq req, Integer serviceId);

    ApiPage<CdkVO> cdkList(CdkApiQueryReq req, Integer serviceId);
}
