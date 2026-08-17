package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 会员中心总览宽表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN)
public class MemberSummaryDO extends BIBaseDO {

    /** 会员总数(人) */
    private Integer memberCount;

    /** 消费数据已确权会员 */
    private Integer verifiedCount;

    /** 是否复购
     * @ext 0/1, 复购数 = SUM(此字段)
     */
    private Integer repurchaseFlag;

    /** 储值余额总额(元) */
    private BigDecimal balanceAmount;

    /** 积分数量 */
    private String pointsCount;
}
