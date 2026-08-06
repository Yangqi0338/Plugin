package com.newzkl.platform.plugin.openapi.domain.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.plugin.openapi.model.command.DeveloperCommand;
import com.newzkl.platform.plugin.openapi.model.entity.Developer;
import com.newzkl.platform.plugin.openapi.model.query.DeveloperQuery;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperRes;
import com.newzkl.platform.plugin.openapi.domain.repository.IDeveloperRepository;
import com.newzkl.platform.plugin.openapi.domain.IDeveloperDomain;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* 开发者
* @author fang
*/
@Service
public class DeveloperDomainImpl implements IDeveloperDomain {

    @Autowired
    private IDeveloperRepository developerRepository;

    @Override
    public Long developerSave(DeveloperCommand developerCommand) {
        Developer developer = TransferUtils.transfer(developerCommand, Developer::new);
        developer.init();
        return developerRepository.developerSave(developer);
    }
    @Override
    public DeveloperRes developerVO(Long developerId) {
        return developerRepository.developerVO(developerId);
    }
    @Override
    public Page<DeveloperRes> developerVOList(DeveloperQuery developerQuery) {
        return developerRepository.developerVOList(developerQuery);
    }

    @Override
    public DeveloperRes developerVOByAppId(String appId) {
        return developerRepository.developerVOByAppId(appId);
    }

    @Override
    public DeveloperRes developerVOByAccountId(Long accountId) {
        return developerRepository.developerVOByAccountId(accountId);
    }
}
