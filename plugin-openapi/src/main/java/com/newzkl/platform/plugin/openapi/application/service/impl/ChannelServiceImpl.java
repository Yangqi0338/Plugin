package com.newzkl.platform.plugin.openapi.application.service.impl;

import com.zkl.scm.finance.rpc.api.virtual.IVirtualAssetsAlterApi;
import com.zkl.scm.finance.rpc.model.req.VirtualAssetsAlterReq;
import com.zkl.scm.model.constants.common.CommonEnum;
import com.zkl.scm.model.constants.finance.FinanceEnum.FinanceUser;
import com.zkl.scm.model.constants.user.AccountErrorCode;
import com.zkl.scm.model.constants.user.RoleEnum;
import com.zkl.scm.model.exception.BaseErrorCode;
import com.zkl.scm.model.exception.ThrowsException;
import com.zkl.scm.model.utils.ScmUtil;
import com.zkl.scm.openapi.application.service.IChannelService;
import com.zkl.scm.rpc.model.ApiPage;
import com.zkl.scm.rpc.user.ThirdUseStoreCdkRes;
import com.zkl.scm.user.rpc.facade.IAccountFacade;
import com.zkl.scm.user.rpc.facade.IRoleFacade;
import com.zkl.scm.user.rpc.model.account.AccountInfo;
import com.zkl.scm.user.rpc.model.channel.ChannelCdkUseReq;
import com.zkl.scm.user.rpc.model.channel.ChannelCodeSyncReq;
import com.zkl.scm.user.rpc.model.channel.ChannelOptionSyncReq;
import com.zkl.scm.user.rpc.model.role.CdkApiQueryReq;
import com.zkl.scm.user.rpc.model.role.CdkVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author muc_fang
 * @Description: 商品
 * @date 2023/12/159:58
 */
@Service
public class ChannelServiceImpl implements IChannelService {

    @DubboReference
    private IRoleFacade roleFacade;
    @DubboReference
    private IVirtualAssetsAlterApi virtualAssetsAlterApi;
    @DubboReference
    private IAccountFacade accountFacade;

    @Override
    public void syncCode(ChannelCodeSyncReq channelCodeSyncReq) {
        roleFacade.jfCreateCdk(RoleEnum.CompanyRole.DEALER.getCode(), channelCodeSyncReq.getPhone(), channelCodeSyncReq.getCodeList());
    }

    @Override
    public void syncOption(Long accountId, ChannelOptionSyncReq req) {
        AccountInfo accountInfo = accountFacade.accountInfo(req.getPhone());
        if(accountInfo == null){
            ThrowsException.exception(AccountErrorCode.NO_EXIST);
        }
        if(!ScmUtil.stringToLongList(accountInfo.getRoleIdList()).contains(RoleEnum.CompanyRole.DEALER.getCode())){
            ThrowsException.exception(BaseErrorCode.PARAM, "当前用户不是交易师");
        }
        //分配期权
        List<VirtualAssetsAlterReq> virtualAssetsAlterReqs = new ArrayList<>();
        VirtualAssetsAlterReq item = new VirtualAssetsAlterReq();
        item.setAccountId(accountInfo.getId());
        item.setAccountName(accountInfo.getUsername());
        item.setAccountType(FinanceUser.TRADERS);
        item.setAssetsType(1);
        item.setAlterType(req.getSymbol());
        item.setAlterValue(req.getValue());
        item.setBusinessType(1);
        String optionMapJson = ScmUtil.getOptionMapJson(CommonEnum.SystemType.HOUSE, 1, req.getValue(), null, null);
        item.setAlterInfo(optionMapJson);
        virtualAssetsAlterReqs.add(item);
        virtualAssetsAlterApi.alterVirtualAssets(virtualAssetsAlterReqs);
    }

    @Override
    public ThirdUseStoreCdkRes useCdk(ChannelCdkUseReq req, Integer serviceId) {
        return roleFacade.useCdk(req, serviceId);
    }

    @Override
    public ApiPage<CdkVO> cdkList(CdkApiQueryReq req, Integer serviceId) {
        return roleFacade.cdkList(req, serviceId);
    }
}
