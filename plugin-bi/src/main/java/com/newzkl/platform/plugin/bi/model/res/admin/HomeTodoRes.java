package com.newzkl.platform.plugin.bi.model.res.admin;

import lombok.Data;

import java.io.Serializable;

/**
 * 首页工作台 - 待办事项
 *
 * <p>对应用户首页「待办事项」卡片, 原型 6 项:
 * 等待付款 / 等待发货 / 售后中 / 库存紧张 / 商品售罄 / 待核验存证。</p>
 */
@Data
public class HomeTodoRes implements Serializable {

    /** 等待付款
     * @ext 补偿增量 SUM: 下单+1, 支付-1
     */
    private Integer waitPayCount;

    /** 等待发货
     * @ext 补偿增量 SUM: 支付+1, 发货-1
     */
    private Integer waitDeliveryCount;

    /** 售后中
     * @ext 补偿增量 SUM: 售后+1, 处理完-1
     */
    private Integer refundingCount;

    /** 库存紧张
     * @ext 补偿增量 SUM: 变紧张+1, 解除-1
     */
    private Integer stockWarnCount;

    /** 商品售罄
     * @ext 补偿增量 SUM: 变售罄+1, 补货-1
     */
    private Integer soldOutCount;

    /** 待核验存证
     * @ext 补偿增量 SUM: 上链+1, 核验完成-1
     */
    private Integer waitVerifyCount;
}
