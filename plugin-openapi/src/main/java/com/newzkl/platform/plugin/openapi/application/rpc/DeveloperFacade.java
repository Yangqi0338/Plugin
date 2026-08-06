package com.newzkl.platform.plugin.openapi.application.rpc;

import com.newzkl.platform.plugin.openapi.model.command.DeveloperCommand;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperRes;
import com.newzkl.platform.plugin.openapi.domain.IDeveloperDomain;
import com.newzkl.platform.plugin.openapi.facade.IDeveloperFacade;
import com.newzkl.platform.plugin.openapi.infrastructure.assembler.DeveloperAssembler;
import com.newzkl.platform.plugin.openapi.model.req.DeveloperInitReq;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

/**
 * @author fang
 */
@Service
public class DeveloperFacade implements IDeveloperFacade {

    private final IDeveloperDomain developerDomain;
    private final DeveloperAssembler assembler;

    public DeveloperFacade(IDeveloperDomain developerDomain, DeveloperAssembler assembler) {
        this.developerDomain = developerDomain;
        this.assembler = assembler;
    }

    @Override
    public DeveloperVO developerVOByAccountId(Long accountId) {
        DeveloperRes developerRes = developerDomain.developerVOByAccountId(accountId);
        if (developerRes == null) {
            return null;
        }
        return assembler.resToVO(developerRes);
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