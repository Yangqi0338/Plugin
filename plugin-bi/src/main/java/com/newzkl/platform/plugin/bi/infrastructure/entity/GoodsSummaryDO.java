package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品中心·总量统计宽表
 *
 * <p>字段 = 总量统计查询展示字段。
 * 实时表: dws_realtime_admin_goods_summary | 日表: dws_day_admin_goods_summary</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN, suffix = "goods_summary")
public class GoodsSummaryDO extends BIBaseDO {

    /** 商品总数 */
    private Integer totalCount;
}
