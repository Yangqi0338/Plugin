package com.newzkl.platform.plugin.openapi.infrastructure.repository;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zkl.scm.model.constants.common.Number;
import com.zkl.scm.openapi.domain.developer.model.entity.Developer;
import com.zkl.scm.openapi.domain.developer.model.query.DeveloperQuery;
import com.zkl.scm.openapi.domain.developer.model.vo.DeveloperVO;
import com.zkl.scm.openapi.domain.developer.repository.IDeveloperRepository;
import com.zkl.scm.openapi.infrastructure.assembler.DeveloperAssembler;
import com.zkl.scm.openapi.infrastructure.dao.DeveloperDAO;
import com.zkl.scm.openapi.infrastructure.entity.DeveloperDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 用户账号
* @author fang
*/
@Repository
public class DeveloperRepositoryImpl implements IDeveloperRepository {

    @Autowired
    private DeveloperDAO developerDAO;
    @Autowired
    private DeveloperAssembler developerAssembler;

    @Override
    public Long developerSave(Developer developer) {
        DeveloperDO developerDO = developerAssembler.domainToDO(developer);
        developerDAO.insert(developerDO);
        return developerDO.getId();
    }
    @Override
    public Developer developer(Long developerId) {
        DeveloperDO developerDO = developerDAO.selectByPrimaryKey(developerId);
        return developerAssembler.doToDomain(developerDO);
    }
    @Override
    public DeveloperVO developerVO(Long developerId) {
         DeveloperDO developerDO = developerDAO.selectByPrimaryKey(developerId);
        return developerAssembler.doToVO(developerDO);
    }
    @Override
    public PageInfo<DeveloperVO> developerVOList(DeveloperQuery developerQuery) {
        if(developerQuery.getPageNo() > Number.ZERO){
            PageHelper.startPage(developerQuery.getPageNo(), developerQuery.getPageSize());
        }
        List<DeveloperVO> developerVO = developerDAO.listByQuery(developerQuery);
        PageInfo<DeveloperVO> page = new PageInfo<DeveloperVO>( developerVO);
        return page;
    }

    @Override
    public DeveloperVO developerVOByAppId(String appId) {
        return developerDAO.developerVOByAppId(appId);
    }

    @Override
    public DeveloperVO developerVOByAccountId(Long accountId) {
        return developerDAO.developerVOByAccountId(accountId);
    }
}
