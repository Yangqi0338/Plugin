package com.newzkl.platform.plugin.openapi.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.io.Serializable;

/**
 * 开发者持久化对象
 *
 * @author muc_fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class DeveloperDO extends BaseDO {
    
    @Index
    private String appId;
    private String appName;
    private String secret;
    @Index
    private Long accountId;
    private String remark;
    private String notifyAddress;
}
