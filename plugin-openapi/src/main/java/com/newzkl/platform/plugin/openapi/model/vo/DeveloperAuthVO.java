package com.newzkl.platform.plugin.openapi.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description: 开发者授权信息
 * @date 2024/1/1814:38
 */
@Data
public class DeveloperAuthVO implements Serializable {
    /**
     * 开发者ID
     */
    private String appId;
    /**
     * 开发者账号ID
     */
    private Long accountId;
    /**
     * 开发者密钥
     */
    private String secret;
}
