package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 首页·营销渠道收入比例宽表(每个渠道一个独立字段)
 *
 * <p>字段 = 营销渠道占比查询的 6 个渠道收入, 事件落库时按渠道写入对应字段,
 * 查询 SUM 各字段后计算占比。不做 channelCode/channelName 聚合。
 * 实时表: dws_realtime_admin_revenue | 日表: dws_day_admin_revenue</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN)
public class RevenueDO extends BIBaseDO {

    /** 微信小程序收入(元)
     */
    private BigDecimal wxMiniAmount;

    /** 公众号收入(元)
     */
    private BigDecimal oaAmount;

    /** H5商城收入(元)
     */
    private BigDecimal h5Amount;

    /** 抖音小程序收入(元)
     */
    private BigDecimal douyinAmount;

    /** APP收入(元)
     */
    private BigDecimal appAmount;

    /** 门店收银台收入(元)
     */
    private BigDecimal posAmount;
}
