package com.newzkl.platform.plugin.audit.workflow.model;

import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import io.soabase.recordbuilder.core.RecordBuilder;

/**
 * 审批账号视图
 *
 * <p>插件自有账号载体, 由 adapter 层从 biz-sys 管理员账号能力转换而来, 隔离 Base 内部账号类型</p>
 *
 * @author KC
 */
@RecordBuilder
public record AuditAccountView(

        /** 账号主键 */
        Long accountId,

        /** 账号名称 */
        String username,

        /** 角色主键 */
        RoleEnum.CompanyRole role
) {
}
