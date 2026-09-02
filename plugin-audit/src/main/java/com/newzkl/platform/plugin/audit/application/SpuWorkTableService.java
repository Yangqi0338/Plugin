package com.newzkl.platform.plugin.audit.application;

import com.newzkl.platform.plugin.audit.domain.SpuWorkTableDomain;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * SPU 工单审批应用服务
 *
 * <p>审批通过的编排入口: 先按工单类型分派到 biz-goods SpuDomain 执行 SPU/SKU 真实变更,
 * 再调用 {@link SpuWorkTableDomain} 更新工单状态, 全链同事务。</p>
 *
 * @author KC
 */
public interface SpuWorkTableService {

    /**
     * 工单审批通过
     *
     * <p>仅待审核单可通过, 防越态/重复执行。按 operateTarget + operateType 分派执行 SPU 主数据变更,
     * skuSalePrice 结构为 {tempId: salePrice}, 非空则按 tempId 回填 SKU 销售价并落审核价快照,
     * 最后置工单审核态为通过。</p>
     *
     * @param workTableId 工单主键
     * @param skuSalePrice 审核录入的 SKU 销售价, 可空
     */
    void pass(Long workTableId, Map<String, String> skuSalePrice);

    /**
     * 工单审批拒绝
     *
     * @param workTableId 工单主键
     * @param reason 拒绝原因
     */
    void refuse(Long workTableId, String reason);
}
