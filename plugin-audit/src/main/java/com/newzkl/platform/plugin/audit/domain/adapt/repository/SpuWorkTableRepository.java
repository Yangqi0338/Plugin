package com.newzkl.platform.plugin.audit.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.plugin.audit.model.dto.SpuWorkTableDTO;
import com.newzkl.platform.plugin.audit.model.query.SpuWorkTableQuery;

/**
 * SPU 工单审批数据仓储
 *
 * @author KC
 */
public interface SpuWorkTableRepository {

    /**
     * 保存工单审批数据
     *
     * @param workTable 工单审批数据
     * @return 工单主键
     */
    Long workTableSave(SpuWorkTableDTO workTable);

    /**
     * 更新工单审批数据
     *
     * @param workTable 工单审批数据 (按 id 更新非空字段)
     * @return 影响行数
     */
    int workTableEdit(SpuWorkTableDTO workTable);

    /**
     * 分页查工单审批数据
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<SpuWorkTableDTO> workTablePage(SpuWorkTableQuery query);

    /**
     * 按主键查工单审批数据
     *
     * @param workTableId 工单主键
     * @return 工单审批数据, 未命中返回 null
     */
    SpuWorkTableDTO workTable(Long workTableId);
}
