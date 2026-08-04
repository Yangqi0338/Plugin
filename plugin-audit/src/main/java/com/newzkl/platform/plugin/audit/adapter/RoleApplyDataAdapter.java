package com.newzkl.platform.plugin.audit.adapter;

import com.newzkl.platform.plugin.audit.port.RoleApplyDataPort;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 角色申请审批数据适配器
 *
 * <p>Base biz-account 现仅有 AuditDataRoleApplyDO 实体, 无对应 Repository/Domain, 无从转调, 全端口按缺基建抛出</p>
 *
 * <ul>
 *   <li>detail: 缺 biz-account 角色申请审批数据读能力</li>
 *   <li>pageJson: 缺 biz-account 角色申请审批数据分页能力</li>
 *   <li>save: 缺 biz-account 角色申请审批数据写能力</li>
 *   <li>oldFlowIds: 缺 biz-account 同组旧审批流查询能力</li>
 * </ul>
 *
 * @author KC
 */
@Component("auditPluginRoleApplyDataAdapter")
public class RoleApplyDataAdapter implements RoleApplyDataPort {

    @Override
    public String detail(Long flowId) {
        throw new UnsupportedOperationException("TODO[infra-gap]: biz-account 缺角色申请审批数据 Repository, detail 无从转调");
    }

    @Override
    public String pageJson(String queryJson) {
        throw new UnsupportedOperationException("TODO[infra-gap]: biz-account 缺角色申请审批数据 Repository, pageJson 无从转调");
    }

    @Override
    public Long save(String dataVoJson) {
        throw new UnsupportedOperationException("TODO[infra-gap]: biz-account 缺角色申请审批数据 Repository, save 无从转调");
    }

    @Override
    public List<Long> oldFlowIds(Long accountId, Long roleId) {
        throw new UnsupportedOperationException("TODO[infra-gap]: biz-account 缺角色申请审批数据 Repository, oldFlowIds 无从转调");
    }
}
