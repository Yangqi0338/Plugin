package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员等级分布宽表
 * @ext 每行一个会员: 会员ID + 等级
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN)
public class MemberLevelDO extends BIBaseDO {

    /** 会员ID */
    private Long memberId;

    /** 等级 */
    private String level;
}
