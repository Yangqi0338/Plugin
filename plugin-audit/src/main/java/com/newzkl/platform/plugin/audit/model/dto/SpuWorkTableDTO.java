package com.newzkl.platform.plugin.audit.model.dto;

import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.model.dto.BaseDTO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import lombok.Data;

import java.util.Map;

/**
 * SPU 工单审批数据
 *
 * <p>SPU 工单审批第二线主数据领域载体: 商品变更提交后落本表, 平台审核, 审核态直接落本表。
 * 审核归属 audit 域, 故实体落 plugin-audit, 与 PromiseFlow 内联模式对齐</p>
 *
 * @author KC
 */
@Data
public class SpuWorkTableDTO extends BaseDTO {
    /**
     * 申请人账号ID
     */
    private Long accountId;
    /**
     * SPU 主键
     */
    private Long spuId;
    /**
     * SPU 名称
     */
    private String spuName;
    /**
     * 操作目标
     */
    private SpuEnum.OperateTarget operateTarget;
    /**
     * 操作类型
     */
    private SpuEnum.OperateType operateType;
    /**
     * 原spu信息
     */
    private SpuVO spuInfo;
    /**
     * SPU编辑信息
     */
    private SpuAuditDTO spuEditInfo;
    /**
     * 审核录入的 SKU 销售价
     */
    private Map<String, String> skuSalePrice;
    /**
     * 审核状态
     */
    private AuditEnum.State auditState;
    /**
     * 审核拒绝原因
     */
    private String auditRefuseReason;
}
