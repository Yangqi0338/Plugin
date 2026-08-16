package com.newzkl.platform.plugin.bi.model.res;

import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员中心总览
 */
@Data
public class MemberOverviewRes implements Serializable {

    /** 会员总数(人) */
    private Integer memberCount;

    /** 消费数据已确权会员 */
    private Integer verifiedCount;

    /** 会员复购率(%)
     * @ext DO 存储是否复购, 复购数/会员数
     */
    private BigDecimal repurchaseRate;

    /** 储值余额总额(元) */
    private Money balanceAmount;

    /** 积分数量 */
    private String pointsCount;
}
