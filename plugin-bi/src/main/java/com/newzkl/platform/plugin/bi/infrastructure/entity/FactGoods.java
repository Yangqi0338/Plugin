package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseIdDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 商品事件事实表(每事件一行, 永久保留)
 *
 * <p>记录商品上架/审核事件, 供待办(待审核商品)与重算。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("fact_goods")
public class FactGoods extends BaseIdDO {

    /** SPU */
    private Long spuId;

    /** 租户 */
    private Long clientId;

    /** 门店 */
    private Long storeId;

    /** 事件类型(SUBMIT/AUDIT/UP/DOWN) */
    private String eventType;

    /** 商品状态(WAIT_AUDIT/ON_SALE/OFF_SALE) */
    private String status;

    /** 事件时间 */
    private LocalDateTime eventTime;
}
