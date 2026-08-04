package com.newzkl.platform.plugin.openapi.application.rpc;

import com.zkl.scm.openapi.domain.developer.model.command.DeveloperCommand;
import com.zkl.scm.openapi.domain.developer.model.vo.DeveloperVO;
import com.zkl.scm.openapi.domain.developer.service.IDeveloperDomain;
import com.zkl.scm.openapi.facade.IDeveloperFacade;
import com.zkl.scm.openapi.model.DeveloperInitReq;
import com.zkl.scm.openapi.model.DeveloperRpcVO;
import com.zkl.scm.web.utils.TransferUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

/**
 * @author fang
 */
@Service
@DubboService
public class DeveloperFacade implements IDeveloperFacade {

    private final IDeveloperDomain developerDomain;

    public DeveloperFacade(IDeveloperDomain developerDomain) {
        this.developerDomain = developerDomain;
    }

    @Override
    public DeveloperRpcVO developerVOByAccountId(Long accountId) {
        DeveloperVO developerVO = developerDomain.developerVOByAccountId(accountId);
        if(developerVO == null){
            return null;
        }
        return TransferUtils.transfer(developerVO, new Function<DeveloperVO, DeveloperRpcVO>() {
            @Override
            public DeveloperRpcVO apply(DeveloperVO developerVO) {
                DeveloperRpcVO developerRpcVO = new DeveloperRpcVO();
                developerRpcVO.setId(developerVO.getId());
                developerRpcVO.setAppName(developerVO.getAppName());
                developerRpcVO.setSecret(developerVO.getSecret());
                developerRpcVO.setAccountId(developerVO.getAccountId());
                developerRpcVO.setRemark(developerVO.getRemark());
                developerRpcVO.setAppId(developerVO.getAppId());
                developerRpcVO.setNotifyAddress(developerVO.getNotifyAddress());
                return developerRpcVO;
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initDeveloper(DeveloperInitReq developerInitReq) {
        DeveloperCommand developerCommand = TransferUtils.transfer(developerInitReq, new Function<DeveloperInitReq, DeveloperCommand>() {
            @Override
            public DeveloperCommand apply(DeveloperInitReq developerInitReq) {
                DeveloperCommand developerCommand = new DeveloperCommand();
                developerCommand.setAppName(developerInitReq.getAppName());
                developerCommand.setSecret(developerInitReq.getSecret());
                developerCommand.setAccountId(developerInitReq.getAccountId());
                developerCommand.setAppId(developerInitReq.getAppId());
                developerCommand.setRemark("");
                return developerCommand;
            }
        });
        developerDomain.developerSave(developerCommand);
    }
}