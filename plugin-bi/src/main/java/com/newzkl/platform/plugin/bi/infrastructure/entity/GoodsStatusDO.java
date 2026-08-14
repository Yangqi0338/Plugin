package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品中心·状态统计宽表
 *
 * <p>字段 = 商品状态统计查询展示字段。
 * 实时表: dws_realtime_admin_goods_status | 日表: dws_day_admin_goods_status</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN, suffix = "goods_status")
public class GoodsStatusDO extends BIBaseDO {

    /** 上架商品数 */
    private Integer onSaleCount;

    /** 下架商品数 */
    private Integer offSaleCount;

    /** 待审核商品数 */
    private Integer pendingAuditCount;
}
