package com.newzkl.platform.plugin.audit.adapter;

import com.newzkl.platform.plugin.audit.port.BrandAuditDataPort;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 品牌申请审批数据适配器
 *
 * <p>Base 当前无 AuditDataBrand 一族实体与仓储, 无从转调, 全端口按缺基建抛出</p>
 *
 * <ul>
 *   <li>detail: 缺 AuditDataBrand 实体与 Repository</li>
 *   <li>pageJson: 缺品牌申请审批数据分页能力</li>
 *   <li>save: 缺品牌申请审批数据写能力</li>
 *   <li>oldFlowIds: 缺同组旧审批流查询能力</li>
 * </ul>
 *
 * @author KC
 */
@Component("auditPluginBrandAuditDataAdapter")
public class BrandAuditDataAdapter implements BrandAuditDataPort {

    @Override
    public String detail(Long flowId) {
        throw new UnsupportedOperationException("TODO[infra-gap]: Base 缺 AuditDataBrand 实体与 Repository, detail 无从转调");
    }

    @Override
    public String pageJson(String queryJson) {
        throw new UnsupportedOperationException("TODO[infra-gap]: Base 缺 AuditDataBrand 实体与 Repository, pageJson 无从转调");
    }

    @Override
    public Long save(String dataVoJson) {
        throw new UnsupportedOperationException("TODO[infra-gap]: Base 缺 AuditDataBrand 实体与 Repository, save 无从转调");
    }

    @Override
    public List<Long> oldFlowIds(Long accountId, Long roleId) {
        throw new UnsupportedOperationException("TODO[infra-gap]: Base 缺 AuditDataBrand 实体与 Repository, oldFlowIds 无从转调");
    }
}
