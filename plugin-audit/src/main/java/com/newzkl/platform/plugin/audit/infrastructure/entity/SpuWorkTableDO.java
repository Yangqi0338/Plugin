package com.newzkl.platform.plugin.audit.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.plugin.audit.model.dto.SpuAuditDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.mpe.autofill.annotation.JsonSerializable;

import java.util.Map;

/**
 * 工单审批数据
 *
 * <p>SPU 工单审批第二线主数据: 商品变更走工单, 平台审核后再生效, 审核态直接落本表。
 * 审核归属 audit 域, 故实体落 plugin-audit, 与 {@link PromiseFlowDO} 内联模式对齐</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(autoResultMap = true)
public class SpuWorkTableDO extends BaseDO {

    /**
     * 申请人账号ID (查询)
     */
    @Index
    private Long accountId;
    /**
     * SPU 主键 (查询)
     */
    @Index
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
    @JsonSerializable
    private SpuVO spuInfo;
    /**
     * SPU编辑信息
     */
    @JsonSerializable
    private SpuAuditDTO spuEditInfo;
    /**
     * 审核录入的 SKU 销售价 JSON
     */
    @JsonSerializable
    private Map<String, String> skuSalePrice;
    /**
     * 审核状态 (查询)
     */
    @Index
    private AuditEnum.State auditState;
    /**
     * 审核拒绝原因
     */
    private String auditRefuseReason;
}
