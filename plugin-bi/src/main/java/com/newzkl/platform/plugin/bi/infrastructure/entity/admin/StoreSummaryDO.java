package com.newzkl.platform.plugin.bi.infrastructure.entity.admin;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.model.annotation.BITableName;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 门店管理·总览宽表
 *
 * <p>字段 = 门店总览查询展示字段。
 * 实时表: dws_realtime_admin_store_summary | 日表: dws_day_admin_store_summary</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@BITableName(client = CommonEnum.Client.ADMIN)
public class StoreSummaryDO extends BIBaseDO {

    /** 店铺总数(家)
     */
    private Integer storeCount;

    /** 待审核(家)
     */
    private Integer pendingAuditCount;

    /** 多门店商户(家)
     */
    private Integer multiStoreCount;

    /** 门店网点合计(个)
     */
    private Integer outletCount;

    /** 已签发数字门店证书
     */
    private Integer certIssuedCount;

    /** 本月新入驻(家)
     */
    private Integer newMonthCount;

    /** 县域服务商渠道(家)
     */
    private Integer countyChannelCount;
}
