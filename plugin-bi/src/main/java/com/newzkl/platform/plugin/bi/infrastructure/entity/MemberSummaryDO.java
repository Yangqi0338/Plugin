package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 会员中心·总览宽表
 *
 * <p>字段 = 会员总览查询展示字段。
 * 实时表: dws_realtime_admin_member_summary | 日表: dws_day_admin_member_summary</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN, suffix = "member_summary")
public class MemberSummaryDO extends BIBaseDO {

    /** 会员总数(人) */
    private Integer memberCount;

    /** 今日新增(人) */
    private Integer todayNewCount;

    /** 已确权会员(人) */
    private Integer verifiedCount;

    /** 确权率(%) */
    private String verifiedRate;

    /** 复购率(%) */
    private String repurchaseRate;

    /** 较上月(百分点) */
    private String repurchaseMoM;

    /** 储值余额(元) */
    private BigDecimal balanceAmount;

    /** 积分总量 */
    private String pointsCount;
}
