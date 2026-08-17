package com.newzkl.platform.plugin.bi.model.res.admin;

import lombok.Data;

import java.io.Serializable;

/**
 * 知链存证总览
 */
@Data
public class EvidenceOverviewRes implements Serializable {

    /** 存证总数 */
    private Integer totalEvidenceCount;

    /** 今日上链(条) */
    private Integer todayOnChainCount;

    /** 待核验(条) */
    private Integer pendingVerifyCount;

    /** 核验通过(条) */
    private Integer verifyPassCount;

    /** 核验失败(条) */
    private Integer verifyFailCount;
}
