package com.newzkl.platform.plugin.openapi.domain;


import com.newzkl.platform.plugin.openapi.model.vo.DeveloperAuthVO;

/**
 * @author muc_fang
 * @Description: 开发模块接口, 需依赖模块自定义实现
 * @date 2023/12/159:34
 */
public interface DeveloperApi {

    /**
     * 获取开发者信息
     * @param appId
     * @return
     */
    DeveloperAuthVO getDeveloperSecret(String appId);
}
