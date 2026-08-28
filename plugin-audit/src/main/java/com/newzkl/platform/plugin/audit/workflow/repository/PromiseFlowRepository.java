package com.newzkl.platform.plugin.audit.workflow.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.plugin.audit.workflow.model.PromiseFlow;
import com.newzkl.platform.plugin.audit.workflow.model.PromiseFlowQuery;

/**
 * 保证金流水仓储
 *
 * @author KC
 */
public interface PromiseFlowRepository {

    /**
     * 保存保证金流水
     *
     * @param promiseFlow 保证金流水
     * @return 流水单主键
     */
    Long promiseFlowSave(PromiseFlow promiseFlow);

    /**
     * 更新保证金流水
     *
     * @param promiseFlow 保证金流水 (按 id 更新非空字段)
     * @return 影响行数
     */
    int promiseFlowEdit(PromiseFlow promiseFlow);

    /**
     * 分页查保证金流水
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<PromiseFlow> promiseFlowPage(PromiseFlowQuery query);

    /**
     * 按主键查保证金流水
     *
     * @param promiseFlowId 流水单主键
     * @return 保证金流水, 未命中返回 null
     */
    PromiseFlow promiseFlow(Long promiseFlowId);
}
