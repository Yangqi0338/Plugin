package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 知链存证·总览宽表
 *
 * <p>字段 = 知链存证查询展示字段。
 * 实时表: dws_realtime_admin_evidence_summary | 日表: dws_day_admin_evidence_summary</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN)
public class EvidenceSummaryDO extends BIBaseDO {

    /** 存证总数
     */
    private Integer totalEvidenceCount;

    /** 今日上链(条)
     */
    private Integer todayOnChainCount;

    /** 待核验(条)
     */
    private Integer pendingVerifyCount;

    /** 核验通过(条)
     */
    private Integer verifyPassCount;

    /** 核验失败(条)
     */
    private Integer verifyFailCount;
}
