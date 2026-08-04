package com.newzkl.platform.plugin.openapi.model.command;

import lombok.Data;

/**
* 开发者
* @author fang
*/
@Data
public class DeveloperCommand {
    /**
     * appId、开发者ID
     */
    private Long id;
    /**
     * 开发者名称
     */
    private String appName;

    private String appId;
    /**
     * 开发者密钥
     */
    private String secret;
    /**
     * 账号ID
     */
    private Long accountId;
    /**
     * 备注
     */
    private String remark;
}
