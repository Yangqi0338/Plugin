package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品中心分类统计宽表
 * @ext 每行一个分类, 商品数量
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN)
public class GoodsCategoryDO extends BIBaseDO {

    /** 分类ID */
    private Long categoryId;

    /** 商品数量 */
    private Integer goodsCount;
}
