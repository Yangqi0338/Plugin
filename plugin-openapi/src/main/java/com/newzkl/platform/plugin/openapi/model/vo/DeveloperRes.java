package com.newzkl.platform.plugin.openapi.model.vo;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 开发者出参
 *
 * @author fang
 */
@Data
public class DeveloperRes extends BaseRes {

    /**
     * 开发者ID
     */
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
