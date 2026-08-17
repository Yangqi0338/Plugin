package com.newzkl.platform.plugin.bi.model.event;

import com.newzkl.platform.plugin.bi.model.enums.BiEventType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * BI 业务事件(通用)
 *
 * <p>业务域 → bi 的事件载体: 会员注册/上链存证/库存变更/商品状态等,
 * 各事件按 {@link BiEventType} 区分, 字段按需取用。
 * TODO: 后续各业务域 tag 稳定后可拆分为独立事件类。</p>
 */
@Data
public class BiBizEvent implements Serializable {

    /** 事件类型 */
    private BiEventType eventType;

    /** 用户ID */
    private Long userId;

    /** 会员ID(会员事件) */
    private Long memberId;

    /** 商品/SPU ID(商品/库存事件) */
    private Long goodsId;

    /** SKU ID(库存事件) */
    private Long skuId;

    /** 门店ID */
    private Long storeId;

    /** 存证ID(上链事件) */
    private Long evidenceId;

    /** 金额(元, 备用) */
    private BigDecimal amount;

    /** 状态(库存: WARN/SOLD_OUT; 商品: ON_SHELF/OFF_SHELF/AUDIT) */
    private String status;

    /** 等级/备用字符串 */
    private String level;
}
