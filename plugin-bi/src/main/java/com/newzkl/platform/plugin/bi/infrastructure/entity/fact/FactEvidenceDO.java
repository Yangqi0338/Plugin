package com.newzkl.platform.plugin.bi.infrastructure.entity.fact;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.plugin.bi.model.enums.BiEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 存证事件事实表(每事件一行)
 *
 * <p>知链存证总数/今日上链/待核验/核验结果 count 的数据源。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FactEvidenceDO extends BaseDO {

    /** 存证ID
     */
    private Long evidenceId;

    /** 事件类型(上链/核验通过/核验失败)
     */
    private BiEventType eventType;

    /** 所属端
     */
    private CommonEnum.Client client;

    /** 用户
     */
    private Long userId;

    /** 业务类型
     */
    private String bizType;

    /** 证书编号
     */
    private String certNo;

    /** 状态
     */
    private String status;
}
