package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;

/**
 * 首页工作台 - 待办事项
 *
 * <p>对应用户首页「待办事项」卡片: 各状态待处理数量。</p>
 */
@Data
public class HomeTodoVO implements Serializable {

    /** 待付款订单数 */
    private Integer waitPayCount;

    /** 待发货订单数 */
    private Integer waitDeliveryCount;

    /** 待收货订单数 */
    private Integer waitReceiveCount;

    /** 售后中订单数 */
    private Integer refundingCount;

    /** 待审核商品数 */
    private Integer waitAuditGoodsCount;

    /** 待审核入驻商户数 */
    private Integer waitAuditMerchantCount;

    /** 库存预警数 */
    private Integer stockWarnCount;

    /** 待核验存证数 */
    private Integer waitVerifyCount;
}
