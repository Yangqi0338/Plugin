package com.newzkl.platform.plugin.audit.port;

import com.newzkl.platform.plugin.audit.workflow.model.AuditAccountView;

/**
 * 管理员账号出站端口
 *
 * <p>取当前登录人与按 ID 查账号, 实现落 adapter 层转调 biz-sys 管理员账号能力</p>
 *
 * @author KC
 */
public interface AdminAccountPort {

    /**
     * 取当前登录人
     *
     * @return 当前登录账号视图
     */
    AuditAccountView currentAccount();

    /**
     * 按主键查账号
     *
     * @param accountId 账号主键
     * @return 账号视图
     */
    AuditAccountView byId(Long accountId);
}
