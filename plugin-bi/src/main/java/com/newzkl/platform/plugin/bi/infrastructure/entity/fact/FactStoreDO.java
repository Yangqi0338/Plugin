package com.newzkl.platform.plugin.bi.infrastructure.entity.fact;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.plugin.bi.domain.constant.BiEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 门店事件事实表(每事件一行)
 *
 * <p>门店管理总数/待审核/本月新入驻 count 的数据源。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FactStoreDO extends BaseDO {

    /** 门店ID */
    private Long storeId;

    /** 事件类型(入驻/审核/证书签发) */
    private BiEventType eventType;

    /** 所属端 */
    private CommonEnum.Client client;

    /** 所属商户 */
    private Long merchantId;

    /** 状态 */
    private String status;
    
}
