package com.newzkl.platform.plugin.openapi.application.service;

import com.newzkl.platform.base.biz.account.model.req.ChannelCodeSyncReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelOptionSyncReq;

/**
 * 渠道商交互业务
 *
 * <p>D-30 能力缺口: 原 scm 尚含 useCdk/cdkList (兑换码), 依赖 CDK 4 模型
 * (ChannelCdkUseReq/CdkApiQueryReq/ThirdUseStoreCdkRes/CdkVO), Base 全仓无对应,
 * 且 IChannelFacade 未建 → 已从接口移除, 待 Base 补 CDK 域能力后回填
 *
 * @author muc_fang
 */
public interface IChannelService {

    void syncCode(ChannelCodeSyncReq channelCodeSyncReq);

    void syncOption(Long accountId, ChannelOptionSyncReq req);
}
