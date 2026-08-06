package com.newzkl.platform.plugin.openapi.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 开发者对外信息
 *
 * @author fang
 */
@Data
public class DeveloperRpcVO implements Serializable {
    /**
     * appId、开发者ID
     */
    private Long id;
    private String appId;
    /**
     * 开发者名称
     */
    private String appName;
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
    /**
     * 通知地址
     */
    private String notifyAddress;
}
