package com.newzkl.platform.plugin.audit.action.cmd;

import com.newzkl.platform.plugin.audit.model.dto.SpuAuditDTO;
import jakarta.validation.constraints.NotNull;

/**
 * SPU 基础信息修改工单提交入参
 * @param spuVO SPU信息,需含 id
 */
public record SpuBaseUpdateRequest(
        @NotNull SpuAuditDTO spuVO) {
}
