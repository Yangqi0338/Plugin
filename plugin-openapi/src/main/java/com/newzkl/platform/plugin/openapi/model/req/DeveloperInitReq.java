package com.newzkl.platform.plugin.openapi.model.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 开发者初始化入参
 *
 * @author muc_fang
 */
@Data
public class DeveloperInitReq implements Serializable {
    private Long accountId;
    private String appName;
    private String appId;
    private String secret;
}
