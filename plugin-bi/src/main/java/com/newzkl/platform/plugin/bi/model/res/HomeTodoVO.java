package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;

/**
 * 首页工作台 - 待办事项
 *
 * <p>对应用户首页「待办事项」卡片, 原型 6 项:
 * 等待付款 / 等待发货 / 售后中 / 库存紧张 / 商品售罄 / 待核验存证。</p>
 */
@Data
public class HomeTodoVO implements Serializable {

    /** 等待付款 */
    private Integer waitPayCount;

    /** 等待发货 */
    private Integer waitDeliveryCount;

    /** 售后中 */
    private Integer refundingCount;

    /** 库存紧张 */
    private Integer stockWarnCount;

    /** 商品售罄 */
    private Integer soldOutCount;

    /** 待核验存证 */
    private Integer waitVerifyCount;
}
