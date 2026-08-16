package com.newzkl.platform.plugin.bi.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * BI 模拟触发事件
 *
 * <p>开发/联调阶段模拟业务事件源(会员注册/上链存证/库存变更/商品状态等),
 * 通过 {@code bi:trigger} tag 触发, 由 {@code BiTriggerConsumer} 消费后写宽表。
 * 生产环境各业务域发真实事件后, 模拟入口废弃。</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BiTriggerEvent implements Serializable {

    /** 模拟事件类型(见 {@link com.newzkl.platform.plugin.bi.domain.constant.BiTriggerType}) */
    private String triggerType;

    /** 业务ID(订单/会员/存证/商品/门店等, 按类型取) */
    private Long bizId;

    /** 用户ID */
    private Long userId;

    /** 门店ID */
    private Long storeId;

    /** 商品ID */
    private Long goodsId;

    /** 会员ID */
    private Long memberId;

    /** 存证ID */
    private Long evidenceId;

    /** 金额(元, 支付/退款/余额等) */
    private BigDecimal amount;

    /** 等级/状态等字符串 */
    private String level;

    /** 状态 */
    private String status;
}
