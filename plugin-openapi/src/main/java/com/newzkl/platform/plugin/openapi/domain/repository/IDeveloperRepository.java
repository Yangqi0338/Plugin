package com.newzkl.platform.plugin.openapi.domain.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.plugin.openapi.model.entity.Developer;
import com.newzkl.platform.plugin.openapi.model.query.DeveloperQuery;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperRes;

/**
* 开发者
* @author fang
*/
public interface IDeveloperRepository {
    /**
     * 开发者-创建
     * @param developer
     * @return
     */
    Long developerSave(Developer developer);
    /**
     * 开发者-实体
     * @param developerId
     * @return
     */
    Developer developer(Long developerId);
    /**
     * 开发者-值对象
     * @param developerId
     * @return
     */
    DeveloperRes developerVO(Long developerId);
    /**
     * 开发者-值对象列表
     * @param developerQuery
     * @return
     */
    Page<DeveloperRes> developerVOList(DeveloperQuery developerQuery);

    DeveloperRes developerVOByAppId(String appId);

    DeveloperRes developerVOByAccountId(Long accountId);
}
