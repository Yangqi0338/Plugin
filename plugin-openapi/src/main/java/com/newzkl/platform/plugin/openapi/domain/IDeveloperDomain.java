package com.newzkl.platform.plugin.openapi.domain;


import com.github.pagehelper.PageInfo;
import com.zkl.scm.openapi.domain.developer.model.command.DeveloperCommand;
import com.zkl.scm.openapi.domain.developer.model.query.DeveloperQuery;
import com.zkl.scm.openapi.domain.developer.model.vo.DeveloperVO;

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
    DeveloperVO developerVO(Long developerId);
    /**
     * 开发者列表
     * @param developerQuery
     * @return
     */
    PageInfo<DeveloperVO> developerVOList(DeveloperQuery developerQuery);

    /**
     * 开发者值对象
     * @param appId
     * @return
     */
    DeveloperVO developerVOByAppId(String appId);

    DeveloperVO developerVOByAccountId(Long accountId);
}
