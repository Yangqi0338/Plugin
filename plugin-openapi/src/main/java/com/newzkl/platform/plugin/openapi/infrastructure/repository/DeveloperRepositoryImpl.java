package com.newzkl.platform.plugin.openapi.infrastructure.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.plugin.openapi.model.entity.Developer;
import com.newzkl.platform.plugin.openapi.model.query.DeveloperQuery;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperRes;
import com.newzkl.platform.plugin.openapi.domain.repository.IDeveloperRepository;
import com.newzkl.platform.plugin.openapi.infrastructure.assembler.DeveloperAssembler;
import com.newzkl.platform.plugin.openapi.infrastructure.dao.DeveloperDAO;
import com.newzkl.platform.plugin.openapi.infrastructure.entity.DeveloperDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


/**
* 用户账号
* @author fang
*/
@Repository
@RequiredArgsConstructor
public class DeveloperRepositoryImpl implements IDeveloperRepository {

    private final DeveloperDAO developerDAO;
    private final DeveloperAssembler developerAssembler;

    @Override
    public Long developerSave(Developer developer) {
        DeveloperDO developerDO = developerAssembler.domainToDO(developer);
        developerDAO.insert(developerDO);
        return developerDO.getId();
    }
    @Override
    public Developer developer(Long developerId) {
        DeveloperDO developerDO = developerDAO.selectById(developerId);
        return developerAssembler.doToDomain(developerDO);
    }
    @Override
    public DeveloperRes developerVO(Long developerId) {
        DeveloperDO developerDO = developerDAO.selectById(developerId);
        return developerAssembler.doToVO(developerDO);
    }
    @Override
    public Page<DeveloperRes> developerVOList(DeveloperQuery developerQuery) {
        Page<DeveloperDO> page = new Page<>(developerQuery.getPageNo(), developerQuery.getPageSize());
        Page<DeveloperDO> doPage = developerDAO.selectPage(page, developerDAO.getLw(developerQuery));
        return TransferUtils.transferPage(doPage, developerAssembler::doToVO);
    }

    @Override
    public DeveloperRes developerVOByAppId(String appId) {
        DeveloperQuery query = new DeveloperQuery();
        query.setAppId(appId);
        DeveloperDO developerDO = developerDAO.selectOne(developerDAO.getLw(query));
        return developerAssembler.doToVO(developerDO);
    }

    @Override
    public DeveloperRes developerVOByAccountId(Long accountId) {
        DeveloperQuery query = new DeveloperQuery();
        query.setAccountId(accountId);
        DeveloperDO developerDO = developerDAO.selectOne(developerDAO.getLw(query).last("LIMIT 1"));
        return developerAssembler.doToVO(developerDO);
    }
}
