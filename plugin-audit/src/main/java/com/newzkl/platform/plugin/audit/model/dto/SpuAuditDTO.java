package com.newzkl.platform.plugin.audit.model.dto;

import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * SPU 工单变更体
 *
 * <p>只收工单可改列, 字段与写侧 {@code SpuDTO} 同名同类型, 保证 TransferUtils 平移不丢字段。
 * 编码/渠道类型/审批态等内部列与 分类名/品牌名/价格区间/库存 等冗余列由 biz-goods 侧生成或回算, 不收入参</p>
 *
 * @author KC
 */
@Data
public class SpuAuditDTO {
    /**
     * ID
     */
    private Long id;
    /**
     * 商品类型 0:实物商品 1:课程 2:服务
     */
    @NotNull
    private Integer goodsType;
    /**
     * 名称
     */
    @NotNull
    private String name;
    /**
     * 标题
     */
    private String title;
    /**
     * 轮播图
     */
    private String scrollImg;
    /**
     * 搜索关键字, 逗号隔开
     */
    private String searchKey;
    /**
     * 图片
     */
    @NotEmpty
    private String img;
    /**
     * 视频
     */
    private String video;
    /**
     * 详情
     */
    private String detail;
    /**
     * 所属平台分类
     */
    @NotNull
    private Long categoryId;
    private String categoryName;
    /**
     * 品牌id
     */
    private Long brandId;
    private String brandName;
    /**
     * 运费模板id
     */
    private Long freightTemplateId;
    /**
     * 发货时效类型
     */
    private SpuEnum.DeliverTimeType deliverTimeType;
    /**
     * 最大发货天数
     */
    private Integer maxDeliverDay;
    /**
     * 销售区域
     */
    private String limitArea;
    /**
     * 规格类型
     *
     * @see SpuEnum.SpecType
     */
    private String specType;
    /**
     * 最小计价数
     */
    private Double minPricingNum;
    /**
     * 状态, 仅状态变更工单使用
     */
    private SpuEnum.State state;
    /**
     * sku列表, 仅 SKU 变更与规格新增工单使用
     */
    private List<SkuAuditDTO> skuList;
    /**
     * 销售属性, 仅规格变更工单使用
     */
    private List<SpuAttributeVO> spuSaleAttributeList;
    /**
     * 参数属性, 仅参数属性变更工单使用
     */
    private List<SpuAttributeVO> spuParamAttributeList;
}
