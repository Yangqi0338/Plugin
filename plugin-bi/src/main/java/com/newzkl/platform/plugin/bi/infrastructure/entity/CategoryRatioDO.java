package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 数据洞察·品类销售占比宽表(每品类一个独立字段)
 *
 * <p>字段 = 品类占比查询展示字段。
 * 实时表: dws_realtime_admin_category_ratio | 日表: dws_day_admin_category_ratio</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN, suffix = "category_ratio")
public class CategoryRatioDO extends BIBaseDO {

    /** 生鲜果蔬占比(%) */
    private BigDecimal freshVegRatio;

    /** 粮油副食占比(%) */
    private BigDecimal grainRatio;

    /** 海鲜水产占比(%) */
    private BigDecimal seafoodRatio;

    /** 乳品冷饮占比(%) */
    private BigDecimal dairyRatio;

    /** 休闲食品占比(%) */
    private BigDecimal snackRatio;

    /** 其他占比(%) */
    private BigDecimal otherRatio;
}
