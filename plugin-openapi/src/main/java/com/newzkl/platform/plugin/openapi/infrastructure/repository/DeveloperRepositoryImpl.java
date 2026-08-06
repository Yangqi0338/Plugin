package com.newzkl.platform.plugin.openapi.infrastructure.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.plugin.openapi.model.entity.Developer;
import com.newzkl.platform.plugin.openapi.model.query.DeveloperQuery;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperVO;
import com.newzkl.platform.plugin.openapi.domain.repository.IDeveloperRepository;
import com.newzkl.platform.plugin.openapi.infrastructure.assembler.DeveloperAssembler;
import com.newzkl.platform.plugin.openapi.infrastructure.dao.DeveloperDAO;
import com.newzkl.platform.plugin.openapi.infrastructure.entity.DeveloperDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


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
    public Page<DeveloperVO> developerVOList(DeveloperQuery developerQuery) {
        Page<DeveloperVO> page = new Page<>(developerQuery.getPageNo(), developerQuery.getPageSize());
        return developerDAO.listByQuery(page, developerQuery);
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
