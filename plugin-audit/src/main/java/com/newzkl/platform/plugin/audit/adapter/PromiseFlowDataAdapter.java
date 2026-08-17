package com.newzkl.platform.plugin.audit.adapter;

import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.plugin.audit.port.PromiseFlowDataPort;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 保证金缴纳审批数据适配器
 *
 * <p>Base 当前无保证金审批实体与仓储, 无从转调, 全端口按缺基建抛出</p>
 *
 * <ul>
 *   <li>detail: 缺保证金审批数据实体与 Repository</li>
 *   <li>pageJson: 缺保证金审批数据分页能力</li>
 *   <li>save: 缺保证金审批数据写能力</li>
 *   <li>oldFlowIds: 缺同组旧审批流查询能力</li>
 * </ul>
 *
 * @author KC
 */
@Component("auditPluginPromiseFlowDataAdapter")
public class PromiseFlowDataAdapter implements PromiseFlowDataPort {

    @Override
    public String detail(Long flowId) {
        throw new UnsupportedOperationException("TODO[infra-gap]: Base 缺保证金审批数据实体与 Repository, detail 无从转调");
    }

    @Override
    public String pageJson(String queryJson) {
        throw new UnsupportedOperationException("TODO[infra-gap]: Base 缺保证金审批数据实体与 Repository, pageJson 无从转调");
    }

    @Override
    public Long save(String dataVoJson) {
        throw new UnsupportedOperationException("TODO[infra-gap]: Base 缺保证金审批数据实体与 Repository, save 无从转调");
    }

    @Override
    public List<Long> oldFlowIds(Long accountId, RoleEnum.CompanyRole role) {
        throw new UnsupportedOperationException("TODO[infra-gap]: Base 缺保证金审批数据实体与 Repository, oldFlowIds 无从转调");
    }
}
