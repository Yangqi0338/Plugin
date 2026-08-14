package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 首页·营销渠道收入比例宽表(按渠道维度)
 *
 * <p>字段 = 营销渠道占比查询展示。每行 = 一个渠道当日收入。
 * 实时表: dws_realtime_admin_revenue_trend | 日表: dws_day_admin_revenue_trend</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN)
public class RevenueDO extends BIBaseDO {

    /** 渠道编码(WX_MINI/OA/H5/DOUYIN/APP/POS) */
    private String channelCode;

    /** 渠道名称 */
    private String channelName;

    /** 收入(元) */
    private BigDecimal amount;
}
