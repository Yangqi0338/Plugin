package com.newzkl.platform.plugin.openapi.model.vo;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.io.Serializable;

/**
 * 开发者授权信息
 *
 * @param appId     开发者ID
 * @param accountId 开发者账号ID
 * @param secret    开发者密钥
 * @author muc_fang
 */
@RecordBuilder
public record DeveloperAuthVO(
        String appId,
        Long accountId,
        String secret
) implements Serializable {
}
