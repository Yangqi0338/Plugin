package com.newzkl.platform.plugin.openapi.application.rpc;

import com.newzkl.platform.base.biz.account.domain.adapt.api.DeveloperApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.DeveloperInitReq;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.plugin.openapi.domain.IDeveloperDomain;
import com.newzkl.platform.plugin.openapi.infrastructure.assembler.DeveloperAssembler;
import com.newzkl.platform.plugin.openapi.model.command.DeveloperCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fang
 */
@Service
@RequiredArgsConstructor
public class AccountDeveloperApi implements DeveloperApi {

    private final IDeveloperDomain developerDomain;
    private final DeveloperAssembler assembler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initDeveloper(DeveloperInitReq developerInitReq) {
        DeveloperCommand developerCommand = TransferUtils.transfer(developerInitReq, DeveloperCommand.class);
        developerDomain.developerSave(developerCommand);
    }
}