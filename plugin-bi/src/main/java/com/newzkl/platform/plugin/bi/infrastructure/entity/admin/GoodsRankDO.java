package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品销售排行宽表
 * @ext 每行一个商品维度, SQL 分组排序取 TOP, 不存 rank
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = AccountEnum.Client.ADMIN)
public class GoodsRankDO extends BIBaseDO {

    /** 商品ID */
    private Long goodsId;

    /** 销量(件) */
    private Integer salesCount;

    /** 销售额(元) */
    private BigDecimal salesAmount;
}
