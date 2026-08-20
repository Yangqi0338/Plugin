package com.newzkl.platform.plugin.bi.infrastructure.entity.fact;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.plugin.bi.model.enums.BiEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 库存事件事实表(每事件一行, 永久保留)
 *
 * <p>记录库存变更事件, 供重算。是否紧张/售罄由 Consumer 落库宽表时判断。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class FactInventoryDO extends BaseDO {

    /** SKU
     */
    private Long skuId;

    /** 所属端
     */
    private AccountEnum.Client client;

    /** 门店
     */
    private Long storeId;

    /** 变更类型
     */
    private BiEventType eventType;

    /** 总库存(变更后)
     */
    private Integer totalStock;

    /** 当前库存(变更后)
     */
    private Integer currentStock;

    /** 变更数量
     */
    private Integer changeCount;
}
