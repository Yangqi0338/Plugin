package com.newzkl.platform.plugin.bi.model.res.admin;

import lombok.Data;

import java.io.Serializable;

/**
 * 门店管理总览
 */
@Data
public class StoreOverviewRes implements Serializable {

    /** 店铺总数(家) */
    private Integer storeCount;

    /** 待审核(家) */
    private Integer pendingAuditCount;

    /** 多门店商户(家) */
    private Integer multiStoreCount;

    /** 门店网点合计(个) */
    private Integer outletCount;

    /** 已签发数字门店证书 */
    private Integer certIssuedCount;

    /** 本月新入驻(家) */
    private Integer newMonthCount;

    /** 县域服务商渠道(家) */
    private Integer countyChannelCount;
}
