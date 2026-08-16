package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 店铺成交排行宽表
 * @ext 每行一个店铺维度, SQL 分组排序取 TOP, 不存 rank
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN)
public class StoreRankDO extends BIBaseDO {

    /** 店铺ID */
    private Long storeId;

    /** 成交额(元) */
    private BigDecimal salesAmount;
}
