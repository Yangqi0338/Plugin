package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员中心·等级分布宽表(每等级一个独立字段)
 *
 * <p>字段 = 等级分布查询展示字段。
 * 实时表: dws_realtime_admin_member_level | 日表: dws_day_admin_member_level</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN, suffix = "member_level")
public class MemberLevelDO extends BIBaseDO {

    /** 普通会员 */
    private Integer normalCount;

    /** 白银会员 */
    private Integer silverCount;

    /** 黄金会员 */
    private Integer goldCount;

    /** 铂金会员 */
    private Integer platinumCount;

    /** 钻石会员 */
    private Integer diamondCount;
}
