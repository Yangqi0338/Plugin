package com.newzkl.platform.plugin.audit.action.cmd;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * SPU 状态修改工单提交入参
 *
 * <p>{@code spuId} 为列表, 支持批量上下架, 每 SPU 落一张工单</p>
 *
 * @param spuId SPU 主键列表
 * @param enable 是否上架, YES 置上架 NO 置下架
 */
public record SpuStateUpdateRequest(
        @NotEmpty List<Long> spuId,
        @NotNull CommonEnum.YesOrNo enable) {
}
