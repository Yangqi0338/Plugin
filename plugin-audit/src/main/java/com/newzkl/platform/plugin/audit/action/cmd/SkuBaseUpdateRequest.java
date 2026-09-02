package com.newzkl.platform.plugin.audit.action.cmd;

import com.newzkl.platform.plugin.audit.model.dto.SkuAuditDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * SKU 基础信息修改工单提交入参
 *
 * <p>落工单 spuEditInfo 的 skuList, 提交时为待新增 SKU 补 tempId, 供审核录价按 tempId 回填</p>
 *
 * @param spuId SPU 主键
 * @param skuVOList 待改 SKU 列表
 */
public record SkuBaseUpdateRequest(
        @NotNull Long spuId,
        @NotEmpty List<SkuAuditDTO> skuVOList) {
}
