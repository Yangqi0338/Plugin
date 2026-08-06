package com.newzkl.platform.plugin.openapi.application.service.impl;

import com.newzkl.platform.base.biz.account.model.req.ChannelCodeSyncReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelOptionSyncReq;
import com.newzkl.platform.plugin.openapi.application.service.IChannelService;
import org.springframework.stereotype.Service;

/**
 * 渠道商交互业务实现
 *
 * <p>D-30 能力缺口: 两法均依赖 Base 未建的出站能力, 暂留桩
 * <ul>
 *   <li>syncCode 原走 {@code IRoleFacade.jfCreateCdk} (交易师建兑换码), Base RoleController 无 jfCreateCdk</li>
 *   <li>syncOption 原含期权分配业务编排 (accountFacade + virtualAssetsAlterApi), 非纯委托,
 *       AccountInfo/VirtualAssetsAlterReq 虽已在 Base 但编排链未迁</li>
 * </ul>
 *
 * @author muc_fang
 */
@Service
public class ChannelServiceImpl implements IChannelService {

    @Override
    public void syncCode(ChannelCodeSyncReq channelCodeSyncReq) {
        // D-30: Base RoleController 缺 jfCreateCdk 能力
        throw new UnsupportedOperationException("syncCode 未接入: Base 缺 jfCreateCdk 能力 (D-30)");
    }

    @Override
    public void syncOption(Long accountId, ChannelOptionSyncReq req) {
        // D-30: 期权分配业务编排未迁 (accountFacade + virtualAssetsAlterApi 链)
        throw new UnsupportedOperationException("syncOption 未接入: 期权分配编排链未迁 (D-30)");
    }
}
