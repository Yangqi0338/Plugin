package com.newzkl.platform.plugin.audit.workflow.model;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;

/**
 * SPU 更新结果
 *
 * <p>插件自有载体, 承载 SPU 变更后新增/更新/删除的 SKU 主键, 供工单处理器发通知时携带受影响 SKU</p>
 *
 * @author KC
 */
@RecordBuilder
public record SpuUpdateResult(

        /** 新增的 SKU 主键列表 */
        List<Long> addSkuIdList,

        /** 更新的 SKU 主键列表 */
        List<Long> updateSkuIdList,

        /** 删除的 SKU 主键列表 */
        List<Long> deleteSkuIdList
) {
}
