package com.newzkl.platform.plugin.openapi.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 开发者持久化对象
 *
 * @author muc_fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("developer")
public class DeveloperDO extends BaseDO implements Serializable {
    
    private String appId;
    private String appName;
    private String secret;
    private Long accountId;
    private String remark;
    private String notifyAddress;
}
