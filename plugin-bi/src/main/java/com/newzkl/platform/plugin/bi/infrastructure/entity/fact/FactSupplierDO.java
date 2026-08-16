package com.newzkl.platform.plugin.bi.infrastructure.entity.fact;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.plugin.bi.domain.constant.BiEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 供应商事件事实表(每事件一行)
 *
 * <p>供应商管理在管/等级/在售/动销/应付 统计的数据源。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FactSupplierDO extends BaseDO {

    /** 供应商ID
     */
    private Long supplierId;

    /** 事件类型(入驻/退出/升级/动销/账期)
     */
    private BiEventType eventType;

    /** 所属端
     */
    private CommonEnum.Client client;

    /** 等级(战略/核心)
     */
    private String level;

    /** 涉及商品数
     */
    private Integer goodsCount;

    /** 动销金额(元)
     */
    private BigDecimal sellAmount;

    /** 应付货款在途(元)
     */
    private BigDecimal payableInTransit;
    
}
