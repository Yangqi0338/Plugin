package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品中心状态统计宽表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = AccountEnum.Client.ADMIN)
public class GoodsStatusDO extends BIBaseDO {

    /** 在售
     * @ext 有库存且上架
     */
    private Integer onSaleCount;

    /** 售罄
     * @ext 库存为 0
     */
    private Integer soldOutCount;

    /** 已上架 */
    private Integer onShelfCount;

    /** 已下架 */
    private Integer offShelfCount;
}
