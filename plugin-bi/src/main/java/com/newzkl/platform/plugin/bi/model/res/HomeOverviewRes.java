package com.newzkl.platform.plugin.bi.model.res;

import cn.hutool.core.util.NumberUtil;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 首页工作台 - 实时概况
 *
 * <p>对应用户首页「实时概况」卡片: 4 项核心指标, 每项含今日值 + 昨日值 + 提升比值。</p>
 */
@Data
public class HomeOverviewRes implements Serializable {

    /** 今日交易额(元)
     */
    private Money todayGmv;

    /** 昨日交易额(元)
     */
    private Money yesterdayGmv;

    /** 今日支付订单(笔)
     */
    private Integer todayPayOrderCount;

    /** 昨日支付订单(笔)
     */
    private Integer yesterdayPayOrderCount;

    /** 今日新增会员(人)
     */
    private Integer todayMemberCount;

    /** 昨日新增会员(人)
     */
    private Integer yesterdayMemberCount;

    /** 今日上链存证(条)
     */
    private Integer todayEvidenceCount;

    /** 昨日上链存证(条)
     */
    private Integer yesterdayEvidenceCount;

    // ==================== 提升比值(动态计算, 不入库) ====================

    /** 交易额较昨日提升比(%) */
    public BigDecimal getGmvMom() {
        return mom(todayGmv, yesterdayGmv);
    }

    /** 支付订单较昨日提升比(%) */
    public BigDecimal getPayOrderMom() {
        return mom(todayPayOrderCount, yesterdayPayOrderCount);
    }

    /** 新增会员较昨日提升比(%) */
    public BigDecimal getMemberMom() {
        return mom(todayMemberCount, yesterdayMemberCount);
    }

    /** 上链存证较昨日提升比(%) */
    public BigDecimal getEvidenceMom() {
        return mom(todayEvidenceCount, yesterdayEvidenceCount);
    }

    /** 今日较昨日提升比: (今日-昨日)/昨日
     * @ext 保留 1 位小数; 昨日为 0 或空返回 null
     */
    private BigDecimal mom(Money today, Money yesterday) {
        if (today == null || today.isNull() || yesterday == null || yesterday.isNull()) {
            return null;
        }
        return mom(today.getAmount(), yesterday.getAmount());
    }

    /** 今日较昨日提升比(整数指标)
     */
    private BigDecimal mom(Integer today, Integer yesterday) {
        if (today == null || yesterday == null) {
            return null;
        }
        return mom(new BigDecimal(today), new BigDecimal(yesterday));
    }

    private BigDecimal mom(BigDecimal t, BigDecimal y) {
        if (y.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return t.subtract(y)
                .multiply(new BigDecimal("100"))
                .divide(y, 1, RoundingMode.HALF_UP);
    }
}
