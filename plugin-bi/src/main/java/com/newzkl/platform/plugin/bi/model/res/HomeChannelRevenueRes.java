package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 首页营销渠道收入比例
 *
 * <p>饼图数据: 各渠道收入平铺为独立字段 + 总收入, 占比由 getXxxRatio() 动态计算。
 * 渠道固定 6 个: 微信小程序/公众号/H5商城/抖音小程序/APP/门店收银台。</p>
 */
@Data
public class HomeChannelRevenueRes implements Serializable {

    /** 总收入(元)
     */
    private BigDecimal totalAmount;

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

    // ==================== 占比(动态计算, 不入库) ====================

    /** 微信小程序占比(%) */
    public BigDecimal getWxMiniRatio() {
        return ratio(wxMiniAmount);
    }

    /** 公众号占比(%) */
    public BigDecimal getOaRatio() {
        return ratio(oaAmount);
    }

    /** H5商城占比(%) */
    public BigDecimal getH5Ratio() {
        return ratio(h5Amount);
    }

    /** 抖音小程序占比(%) */
    public BigDecimal getDouyinRatio() {
        return ratio(douyinAmount);
    }

    /** APP占比(%) */
    public BigDecimal getAppRatio() {
        return ratio(appAmount);
    }

    /** 门店收银台占比(%) */
    public BigDecimal getPosRatio() {
        return ratio(posAmount);
    }

    /** 渠道占比 = 渠道收入/总收入*100
     * @ext 保留 1 位小数; 总收入为 0 或空返回 null
     */
    private BigDecimal ratio(BigDecimal channelAmount) {
        if (channelAmount == null || totalAmount == null
                || totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return channelAmount.multiply(new BigDecimal("100"))
                .divide(totalAmount, 1, RoundingMode.HALF_UP);
    }
}
