package com.newzkl.platform.plugin.openapi.facade;

import com.newzkl.platform.plugin.openapi.model.req.DeveloperInitReq;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperVO;

/**
 * 开发者对外门面
 *
 * @author fang
 */
public interface IDeveloperFacade {
    /**
     * 开发者信息
     *
     * @param accountId 账号ID
     * @return 开发者对外信息
     */
    DeveloperVO developerVOByAccountId(Long accountId);

    /**
     * 初始化开发者
     *
     * @param developerInitReq 初始化入参
     */
    void initDeveloper(DeveloperInitReq developerInitReq);
}
