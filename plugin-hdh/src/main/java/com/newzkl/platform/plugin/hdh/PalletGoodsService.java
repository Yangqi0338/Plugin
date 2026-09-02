package com.newzkl.platform.plugin.hdh;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuExpandVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuSaleAttributeVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.biz.goods.model.biz.vo.CategoryLayerVO;
import com.newzkl.platform.plugin.hdh.model.PalletCategoryQuery;
import com.newzkl.platform.plugin.hdh.model.PalletSpuQuery;
import com.newzkl.platform.plugin.hdh.model.req.HuiDingHuoGetCategoryListReq;
import com.newzkl.platform.plugin.hdh.model.req.HuiDingHuoProductBatchGetReq;
import com.newzkl.platform.plugin.hdh.model.req.HuiDingHuoProductGetReq;
import com.newzkl.platform.plugin.hdh.model.res.HuiDingHuoGetCategoryListRes;
import com.newzkl.platform.plugin.hdh.model.res.HuiDingHuoProductBatchRes;
import com.newzkl.platform.plugin.hdh.model.res.HuiDingHuoProductDetailRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 货盘选品服务
 *
 * <p>迁自 new-scm scm-goods {@code GoodsQueryServiceImpl} 的 palletSpuPage/palletSpuDetail/palletCategoryPage,
 * 走会订货 {@link HuiDingHuoApiUtils} 外部 API 取商品/分类, 转 Base SpuVO/CategoryLayerVO</p>
 *
 * <p>⚠️ 契约变更: 源返 PageHelper PageInfo, 本仓按 rules/Architecture.md 改 MyBatis-Plus Page
 * (records/total/current/size)</p>
 *
 * <p>⚠️ 能力降级: 源 buildSpuVOByHdhItem 末尾用 spuRepository.spuIdByQuery 按 outSpuId 反查本系统
 * 已选品 SPU 回填 choose 标识, plugin-hdh 无 biz-goods spuId 反查端口, choose 恒 false (登记 deferred)</p>
 *
 * @author KC
 */
@Slf4j
@Service
public class PalletGoodsService {

    /** 会订货默认渠道类型 */
    private static final int HDH_FIX_CHANNEL_TYPE = 2;

    /**
     * 货盘 SPU 分页
     *
     * @param spuQuery 查询条件
     * @return SPU 分页
     */
    public Page<SpuVO> palletSpuPage(PalletSpuQuery spuQuery) {
        SpuEnum.SpuChannelSource channelSource = SpuEnum.SpuChannelSource.findByCode(spuQuery.getSpuChannelSource());
        Page<SpuVO> page = new Page<>(spuQuery.getPageNo(), spuQuery.getPageSize());
        if (channelSource != SpuEnum.SpuChannelSource.HDH) {
            return page;
        }
        HuiDingHuoProductBatchGetReq req = new HuiDingHuoProductBatchGetReq();
        req.setPage(spuQuery.getPageNo());
        req.setLimit(spuQuery.getPageSize());
        req.setKeyword(spuQuery.getName());
        req.setCateId1(spuQuery.getCateId1());
        req.setCateId2(spuQuery.getCateId2());
        req.setCateId3(spuQuery.getCateId3());
        List<Integer> channelTypes = Opt.ofEmptyAble(spuQuery.getChannelTypes())
                .orElse(CollUtil.newArrayList(HDH_FIX_CHANNEL_TYPE));
        req.setChannelTypes(channelTypes);

        HuiDingHuoProductBatchRes productListRes = HuiDingHuoApiUtils.batchGetProducts(req);
        if (productListRes.getSuccess() == 1 && productListRes.getData() != null) {
            HuiDingHuoProductBatchRes.DataDTO data = productListRes.getData();
            page.setTotal(data.getTotal());
            List<SpuVO> spuList = data.getItemList().stream()
                    .map(it -> buildSpuVOByHdhItem(it, channelTypes))
                    .collect(Collectors.toList());
            page.setRecords(spuList);
        }
        return page;
    }

    /**
     * 货盘 SPU 详情
     *
     * @param spuQuery 查询条件 (需 outSpuId)
     * @return SPU 详情, 未命中返 null
     */
    public SpuVO palletSpuDetail(PalletSpuQuery spuQuery) {
        SpuEnum.SpuChannelSource channelSource = SpuEnum.SpuChannelSource.findByCode(spuQuery.getSpuChannelSource());
        if (channelSource != SpuEnum.SpuChannelSource.HDH) {
            return null;
        }
        HuiDingHuoProductGetReq req = new HuiDingHuoProductGetReq();
        req.setIds(CollUtil.newArrayList(spuQuery.getOutSpuId()));
        List<Integer> channelTypes = Opt.ofEmptyAble(spuQuery.getChannelTypes())
                .orElse(CollUtil.newArrayList(HDH_FIX_CHANNEL_TYPE));
        HuiDingHuoProductDetailRes productRes = HuiDingHuoApiUtils.getProductById(req);
        if (productRes.getSuccess() == 1 && CollUtil.isNotEmpty(productRes.getData())) {
            HuiDingHuoProductDetailRes.Item data = CollUtil.getFirst(productRes.getData());
            return buildSpuVOByHdhItem(BeanUtil.copyProperties(data, HuiDingHuoProductBatchRes.Item.class), channelTypes);
        }
        return null;
    }

    /**
     * 货盘分类树
     *
     * @param categoryQuery 查询条件
     * @return 分类树
     */
    public List<CategoryLayerVO> palletCategoryList(PalletCategoryQuery categoryQuery) {
        HuiDingHuoGetCategoryListReq req = new HuiDingHuoGetCategoryListReq();
        HuiDingHuoGetCategoryListRes categoryList = HuiDingHuoApiUtils.getCategoryList(req);
        List<CategoryLayerVO> categoryVOList = new ArrayList<>();
        if (categoryList.getSuccess() == 1 && CollUtil.isNotEmpty(categoryList.getData())) {
            categoryList.getData().forEach(category ->
                    categoryVOList.add(buildCategoryVOByHdhCategory(category, 0L)));
        }
        return categoryVOList;
    }

    /**
     * 会订货分类转 CategoryLayerVO (递归子树)
     *
     * @param category 会订货分类
     * @param pid      父 ID
     * @return 分类 VO
     */
    private CategoryLayerVO buildCategoryVOByHdhCategory(HuiDingHuoGetCategoryListRes.Category category, Long pid) {
        CategoryLayerVO categoryVO = new CategoryLayerVO();
        categoryVO.setId(category.getId());
        categoryVO.setPid(pid);
        categoryVO.setName(category.getName());
        categoryVO.setImg(category.getLogoUrl());
        if (CollUtil.isNotEmpty(category.getSubCategoryList())) {
            categoryVO.setChildren(category.getSubCategoryList().stream()
                    .map(it -> buildCategoryVOByHdhCategory(it, category.getId()))
                    .collect(Collectors.toList()));
        }
        return categoryVO;
    }

    /**
     * 会订货商品转 Base SpuVO
     *
     * <p>价格由源 int(分) 改 Money.of(cent); channelType 由 int 改 SpuEnum.ChannelType 枚举;
     * choose 因缺 spuId 反查端口恒 false (deferred)</p>
     *
     * @param item         会订货商品
     * @param channelTypes 渠道类型过滤
     * @return SpuVO
     */
    private SpuVO buildSpuVOByHdhItem(HuiDingHuoProductBatchRes.Item item, List<Integer> channelTypes) {
        SpuVO spuVO = new SpuVO();
        if (item == null) {
            return spuVO;
        }
        List<String> imageList = item.getImageList();
        List<String> sellingPoints = item.getSellingPoints();
        List<HuiDingHuoProductBatchRes.Sku> itemSkuList = item.getSkuList();

        spuVO.setOutSpuId(item.getId());
        spuVO.setName(item.getName());
        spuVO.setTitle(item.getOriginalName());
        spuVO.setScrollImg(StrUtil.join(",", imageList));
        spuVO.setSearchKey(StrUtil.join(",", sellingPoints));
        spuVO.setImg(CollUtil.getFirst(imageList));
        spuVO.setChannelType(SpuEnum.ChannelType.OUT);
        spuVO.setGoodsType(SpuEnum.SaleType.REAL.getCode());
        spuVO.setAccountId(Long.valueOf(SpuEnum.SpuChannelSource.HDH.getCode()));
        spuVO.getExpand().setCategoryName(StrJoiner.of(" / ").setNullMode(StrJoiner.NullMode.IGNORE)
                .append(item.getFirstCateName())
                .append(item.getSecondCateName())
                .append(item.getThirdCateName())
                .toString());
        spuVO.setDetail(CollUtil.join(PatternUtil.getHtmlImgList(item.getRichDesc()), ","));
        spuVO.setState(SpuEnum.State.STORE.getCode());
        spuVO.setOutState(item.getShelfStatus());
        spuVO.setFreightTemplateId(SpuEnum.SpuChannelSource.HDH.getCode().longValue());

        List<SpuAttributeVO> spuAttributeList = new ArrayList<>();
        List<HuiDingHuoProductBatchRes.SkuNameValue> skuNameValues = item.getSkuNameValues();
        if (CollUtil.isNotEmpty(skuNameValues)) {
            skuNameValues.forEach(skuNameValue -> {
                SpuAttributeVO spuAttributeVO = new SpuAttributeVO();
                spuAttributeVO.setType(1);
                spuAttributeVO.setName(skuNameValue.getSkuName());
                spuAttributeVO.setValue(skuNameValue.getSkuValues().stream()
                        .map(HuiDingHuoProductBatchRes.SkuValue::getSkuValue)
                        .collect(Collectors.joining(",")));
                spuAttributeList.add(spuAttributeVO);
            });
        }

        List<SkuVO> skuList = buildSkuList(itemSkuList, channelTypes);
        if (CollUtil.isNotEmpty(itemSkuList)) {
            List<Money> originalPriceList = skuList.stream().map(SkuVO::getMarketPrice)
                    .filter(Objects::nonNull).sorted(Comparator.comparing(Money::getCent))
                    .collect(Collectors.toList());
            List<Money> priceList = skuList.stream().map(SkuVO::getSupplyPrice)
                    .filter(Objects::nonNull).sorted(Comparator.comparing(Money::getCent))
                    .collect(Collectors.toList());

            spuVO.getExpand().setMarketPriceBegan(CollUtil.getFirst(originalPriceList));
            spuVO.setMarketPrice(Opt.ofNullable(spuVO.getExpand().getMarketPriceBegan()).orElse(Money.of(0)));
            spuVO.getExpand().setMarketPriceEnd(CollUtil.getLast(originalPriceList));

            spuVO.getExpand().setSupplierPriceBegan(CollUtil.getFirst(priceList));
            spuVO.setSupplyPrice(Opt.ofNullable(spuVO.getExpand().getSupplierPriceBegan()).orElse(Money.of(0)));
            spuVO.getExpand().setSupplierPriceEnd(CollUtil.getLast(priceList));
            // 源 spuVO.setInventory(sum totalStock): Base SpuVO 无 inventory 字段, 略
        }
        spuVO.setSkuList(skuList);

        skuList.stream().flatMap(it -> it.getSaleAttribute().stream())
                .collect(Collectors.groupingBy(SkuSaleAttributeVO::getName))
                .forEach((name, sameNameList) -> {
                    SpuAttributeVO spuAttributeVO = new SpuAttributeVO();
                    spuAttributeVO.setType(0);
                    spuAttributeVO.setName(name);
                    spuAttributeVO.setValue(JSONUtil.toJsonStr(sameNameList.stream()
                            .map(SkuSaleAttributeVO::getValue).distinct().collect(Collectors.toList())));
                    spuAttributeList.add(spuAttributeVO);
                });

        spuVO.setSpuSaleAttributeList(spuAttributeList.stream()
                .filter(a -> a.getType() == 0).collect(Collectors.toList()));
        spuVO.setSpuParamAttributeList(spuAttributeList.stream()
                .filter(a -> a.getType() == 1).collect(Collectors.toList()));

        spuVO.setLimitBuy((item.getItemLimitCondition() != null
                && item.getItemLimitCondition().getCycleLimitQuantity() != null) ? 1 : 0);

        // TODO[capability-gap]: 源用 spuRepository.spuIdByQuery(outSpuId) 回填 choose (已选品标识),
        // plugin-hdh 无 biz-goods spuId 反查端口, choose 保持默认 false
        return spuVO;
    }

    /**
     * 会订货 SKU 列表转 Base SkuVO 列表
     *
     * <p>价格 originalPrice/price(元) × 100 → Money.of(cent); 取 profit 最高渠道定价;
     * 无命中 channelType 的 SKU 跳过</p>
     *
     * @param itemSkuList  会订货 SKU
     * @param channelTypes 渠道类型过滤
     * @return SkuVO 列表
     */
    private List<SkuVO> buildSkuList(List<HuiDingHuoProductBatchRes.Sku> itemSkuList, List<Integer> channelTypes) {
        List<SkuVO> skuList = new ArrayList<>();
        if (CollUtil.isEmpty(itemSkuList)) {
            return skuList;
        }
        for (HuiDingHuoProductBatchRes.Sku itemSku : itemSkuList) {
            List<HuiDingHuoProductBatchRes.SkuChannel> skuChannels = itemSku.getSkuChannels().stream()
                    .filter(it -> channelTypes.contains(NumberUtil.parseInt(it.getChannelType())))
                    .collect(Collectors.toList());
            if (CollUtil.isEmpty(skuChannels)) {
                continue;
            }
            SkuVO skuVO = new SkuVO();
            SkuExpandVO skuExpandVO = new SkuExpandVO();
            skuExpandVO.setUnit(itemSku.getUnitQuantity() + itemSku.getUnit());

            List<SkuSaleAttributeVO> saleAttributeList = new ArrayList<>();
            List<HuiDingHuoProductBatchRes.SkuDetailValue> skuValues = itemSku.getSkuValues();
            if (CollUtil.isNotEmpty(skuValues)) {
                skuValues.forEach(it -> {
                    SkuSaleAttributeVO attributeVO = new SkuSaleAttributeVO();
                    attributeVO.setName(it.getSkuName());
                    attributeVO.setValue(it.getSkuValue());
                    skuVO.setImg(it.getImageUrl());
                    saleAttributeList.add(attributeVO);
                });
            }
            SkuSaleAttributeVO unitAttributeVO = new SkuSaleAttributeVO();
            unitAttributeVO.setName("件数");
            unitAttributeVO.setValue(skuExpandVO.getUnit());
            saleAttributeList.add(unitAttributeVO);

            skuChannels.stream()
                    .max(Comparator.comparing(HuiDingHuoProductBatchRes.SkuChannel::getProfit))
                    .ifPresent(skuChannel -> {
                        skuVO.setMarketPrice(Money.of(NumberUtil.mul(skuChannel.getOriginalPrice(),
                                new java.math.BigDecimal(100)).intValue()));
                        skuVO.setSupplyPrice(Money.of(NumberUtil.mul(skuChannel.getPrice(),
                                new java.math.BigDecimal(100)).intValue()));
                        skuVO.setBuyStartQty(skuChannel.getMinBuyNum());
                        skuExpandVO.setChannelType(skuChannel.getChannelType());
                        skuExpandVO.setItemCode(skuChannel.getItemCode());
                    });

            skuVO.setSaleAttribute(saleAttributeList);
            skuVO.setExpand(skuExpandVO);
            skuVO.setOutSkuId(itemSku.getSkuId());
//            skuVO.setBarCode(itemSku.getUpc());
//            skuVO.setWeight(NumberUtil.div(itemSku.getWeightG(), new java.math.BigDecimal(1000), 2,
//                    RoundingMode.HALF_UP).doubleValue());
            skuList.add(skuVO);
        }
        return skuList;
    }
}
