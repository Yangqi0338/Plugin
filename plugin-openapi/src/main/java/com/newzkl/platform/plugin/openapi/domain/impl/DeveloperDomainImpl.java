package com.newzkl.platform.plugin.openapi.domain.impl;


import com.github.pagehelper.PageInfo;
import com.zkl.scm.openapi.domain.developer.model.command.DeveloperCommand;
import com.zkl.scm.openapi.domain.developer.model.entity.Developer;
import com.zkl.scm.openapi.domain.developer.model.query.DeveloperQuery;
import com.zkl.scm.openapi.domain.developer.model.vo.DeveloperVO;
import com.zkl.scm.openapi.domain.developer.repository.IDeveloperRepository;
import com.zkl.scm.openapi.domain.developer.service.IDeveloperDomain;
import com.zkl.scm.web.utils.TransferUtils;
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
    public DeveloperVO developerVO(Long developerId) {
        return developerRepository.developerVO(developerId);
    }
    @Override
    public PageInfo<DeveloperVO> developerVOList(DeveloperQuery developerQuery) {
        return developerRepository.developerVOList(developerQuery);
    }

    @Override
    public DeveloperVO developerVOByAppId(String appId) {
        return developerRepository.developerVOByAppId(appId);
    }

    @Override
    public DeveloperVO developerVOByAccountId(Long accountId) {
        return developerRepository.developerVOByAccountId(accountId);
    }
}
