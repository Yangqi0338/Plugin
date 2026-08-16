package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品中心总量统计宽表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN)
public class GoodsSummaryDO extends BIBaseDO {

    /** 商品总数(SPU) */
    private Integer spuCount;

    /** SKU总数 */
    private Integer skuCount;

    /** 已上链存证商品 */
    private Integer onChainGoodsCount;

    /** 一物一码绑定
     * @ext 上链后生成码, 一个商品状态
     */
    private Integer oneCodeBindCount;

    /** 今日新增商品
     * @ext 实时表 spu_count
     */
    private Integer todayNewSpuCount;
}
