package com.newzkl.platform.plugin.audit.port;

import java.util.List;

/**
 * SPU 工单审批数据出站端口
 *
 * <p>工单审批数据读写, 实现落 adapter 层转调 biz-goods AuditDataWorkTableRepository 与 WorkTableWorkflowDomain</p>
 *
 * @author KC
 */
public interface WorktableDataPort {

    /**
     * 保存工单审批数据
     *
     * @param dataVoJson 工单审批数据 JSON, 结构同 biz-goods AuditDataWorkTableVO
     * @return 审批数据主键
     */
    Long save(String dataVoJson);

    /**
     * 查工单审批数据详情
     *
     * @param id 审批数据主键
     * @return 工单审批数据 JSON
     */
    String detail(Long id);

    /**
     * 按查询条件列出工单审批数据
     *
     * @param queryJson 查询条件 JSON, 结构同 biz-goods AuditDataWorkTableQuery
     * @return 工单审批数据 JSON 列表
     */
    List<String> listByQuery(String queryJson);

    /**
     * 分页查工单审批数据
     *
     * @param queryJson 查询条件 JSON, 结构同 biz-goods AuditDataWorkTableQuery
     * @return 分页结果 JSON
     */
    String pageJson(String queryJson);

    /**
     * 删除工单审批数据
     *
     * @param ids 审批数据主键列表
     */
    void delete(List<Long> ids);

    /**
     * 修改工单审批数据的 SKU 销售价快照
     *
     * @param id 审批数据主键
     * @param skuSalePriceJson SKU 销售价 JSON
     */
    void editSkuSalePrice(Long id, String skuSalePriceJson);
}
