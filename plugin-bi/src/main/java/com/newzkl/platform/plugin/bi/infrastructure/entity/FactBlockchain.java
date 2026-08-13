package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseIdDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 知链存证事件事实表(每事件一行, 永久保留)
 *
 * <p>记录"上链完成"事件本身的度量。字段语义由知链事件决定(bi 需要上链数 → 事件即一条)。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("fact_blockchain")
public class FactBlockchain extends BaseIdDO {

    /** 存证业务ID */
    private Long evidenceId;

    /** 事件类型(EVIDENCE/VERIFY) */
    private String eventType;

    /** 租户 */
    private Long clientId;

    /** 用户 */
    private Long userId;

    /** 存证类型(订单/资质/素材) */
    private String bizType;

    /** 存证编号 */
    private String certNo;

    /** 状态(上链成功/核验通过) */
    private String status;

    /** 事件时间 */
    private LocalDateTime eventTime;
}
