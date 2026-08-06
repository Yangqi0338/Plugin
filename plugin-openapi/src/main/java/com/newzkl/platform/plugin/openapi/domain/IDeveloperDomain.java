package com.newzkl.platform.plugin.openapi.domain;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.plugin.openapi.model.command.DeveloperCommand;
import com.newzkl.platform.plugin.openapi.model.query.DeveloperQuery;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperRes;

/**
* 开发者
* @author fang
*/
public interface IDeveloperDomain {
    /**
     * 开发者创建
     * @param developerCommand
     * @return
     */
    Long developerSave(DeveloperCommand developerCommand);
    /**
     * 开发者值对象
     * @param developerId
     * @return
     */
    DeveloperRes developerVO(Long developerId);
    /**
     * 开发者列表
     * @param developerQuery
     * @return
     */
    Page<DeveloperRes> developerVOList(DeveloperQuery developerQuery);

    /**
     * 开发者值对象
     * @param appId
     * @return
     */
    DeveloperRes developerVOByAppId(String appId);

    DeveloperRes developerVOByAccountId(Long accountId);
}
