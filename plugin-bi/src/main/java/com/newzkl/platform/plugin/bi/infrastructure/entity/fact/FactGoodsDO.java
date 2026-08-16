package com.newzkl.platform.plugin.bi.infrastructure.entity.fact;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.plugin.bi.domain.constant.BiEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品事件事实表(每事件一行, 永久保留)
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class FactGoodsDO extends BaseDO {

    /** SPU
     */
    private Long spuId;

    /** 所属端
     */
    private CommonEnum.Client client;

    /** 门店
     */
    private Long storeId;

    /** 事件类型(SUBMIT/AUDIT/UP/DOWN)
     */
    private BiEventType eventType;

    /** 商品状态(WAIT_AUDIT/ON_SALE/OFF_SALE)
     */
    private String status;
    
}
