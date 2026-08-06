package com.newzkl.platform.plugin.openapi.model.vo;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.io.Serializable;

/**
 * 开发者对外信息
 *
 * @param id            开发者ID
 * @param appId         appId
 * @param appName       开发者名称
 * @param secret        开发者密钥
 * @param accountId     账号ID
 * @param remark        备注
 * @param notifyAddress 通知地址
 * @author fang
 */
@RecordBuilder
public record DeveloperVO(
        Long id,
        String appId,
        String appName,
        String secret,
        Long accountId,
        String remark,
        String notifyAddress
) implements Serializable {
}
