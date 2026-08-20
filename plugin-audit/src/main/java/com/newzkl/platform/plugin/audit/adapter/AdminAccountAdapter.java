package com.newzkl.platform.plugin.audit.adapter;

import com.newzkl.platform.base.biz.account.domain.service.AccountDomain;
import com.newzkl.platform.base.biz.account.model.vo.AccountVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.plugin.audit.port.AdminAccountPort;
import com.newzkl.platform.plugin.audit.workflow.model.AuditAccountView;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 管理员账号适配器
 *
 * <p>currentAccount 取自登录态 SecurityUtils; byId 转调 biz-sys AdminAccountDomain</p>
 *
 * <p>AdminAccountRes 无独立 role 字段, 仅有 aroleIdList (逗号分隔角色串), byId 取首个角色为 role 尽力而为</p>
 *
 * @author KC
 */
@Component("auditPluginAdminAccountAdapter")
@RequiredArgsConstructor
public class AdminAccountAdapter implements AdminAccountPort {

    private final AccountDomain accountDomain;

    @Override
    public AuditAccountView currentAccount() {
        return new AuditAccountView(SecurityUtils.getAccountId(), SecurityUtils.getUsername(), SecurityUtils.getIdentity());
    }

    @Override
    public AuditAccountView byId(Long accountId) {
        AccountVO res = accountDomain.account(AccountEnum.Client.ADMIN, accountId);
        if (res == null) {
            return null;
        }
        return new AuditAccountView(res.getId(), res.getUsername(), null);
    }

    /**
     * 从逗号分隔角色串取首个角色主键
     *
     * @param aroleIdList 逗号分隔角色主键串
     * @return 首个角色主键, 空串返回 null
     */
    private Long firstRoleId(String aroleIdList) {
        if (StringUtils.isBlank(aroleIdList)) {
            return null;
        }
        String first = StringUtils.split(aroleIdList, ',')[0].trim();
        return StringUtils.isNumeric(first) ? Long.parseLong(first) : null;
    }
}
