package com.newzkl.platform.plugin.audit.model.dto;

import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuSaleAttributeVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * SKU 工单变更体
 *
 * <p>只收 SKU 可改列, 字段与写侧 {@code SkuDTO} 同名同类型, 保证 TransferUtils 平移不丢字段。
 * 价格统一 Money, 审核录价按 tempId 回填 salePrice</p>
 *
 * @author KC
 */
@Data
public class SkuAuditDTO {
    /**
     * ID, 为空视为待新增 SKU
     */
    private Long id;
    /**
     * 临时ID, 提交时生成, 供审核录价按此键回填 salePrice
     */
    private String tempId;
    /**
     * 图片
     */
    private String img;
    /**
     * 名称
     */
    @NotEmpty
    private String name;
    /**
     * 商品销售属性
     */
    @NotEmpty
    private List<SkuSaleAttributeVO> saleAttribute;
    /**
     * 市场价
     */
    @NotNull
    private Money marketPrice;
    /**
     * 供货价
     */
    private Money supplyPrice;
    /**
     * 销售价(to channel), 平台审核时录入
     */
    private Money salePrice;
    /**
     * 建议零售价(to c)
     */
    private Money unitPrice;
    /**
     * 起购数量
     */
    private Integer buyStartQty;
}
