package com.newzkl.platform.plugin.bi.infrastructure.entity.fact;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.plugin.bi.domain.constant.BiEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 知链存证事件事实表(每事件一行, 永久保留)
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class FactBlockchainDO extends BaseDO {

    /** 存证业务ID */
    private Long evidenceId;

    /** 事件类型(EVIDENCE/VERIFY) */
    private BiEventType eventType;

    /** 所属端 */
    private CommonEnum.Client client;

    /** 用户 */
    private Long userId;

    /** 存证类型 */
    private String bizType;

    /** 存证编号 */
    private String certNo;

    /** 状态 */
    private String status;
}
