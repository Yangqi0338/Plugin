package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseIdDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 库存事件事实表(每事件一行, 永久保留)
 *
 * <p>记录库存变更事件(入库/出库/扣减), 供待办(库存紧张/售罄)与重算。
 * 是否紧张由 bi 判断: 剩余≤10% 紧张, 当前=0 售罄。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("fact_inventory")
public class FactInventory extends BaseIdDO {

    /** SKU */
    private Long skuId;

    /** 租户 */
    private Long clientId;

    /** 门店 */
    private Long storeId;

    /** 变更类型(IN/OUT/DEDUCT/RETURN) */
    private String changeType;

    /** 总库存(变更后) */
    private Integer totalStock;

    /** 当前库存(变更后) */
    private Integer currentStock;

    /** 变更数量 */
    private Integer changeCount;

    /** 事件时间 */
    private LocalDateTime eventTime;
}
