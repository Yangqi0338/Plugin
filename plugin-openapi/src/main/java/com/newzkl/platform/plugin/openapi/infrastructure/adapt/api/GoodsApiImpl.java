package com.newzkl.platform.plugin.openapi.infrastructure.adapt.api;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuCategoryService;
import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuService;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.SpuRepository;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.biz.goods.model.goods.query.spu.SpuCategoryQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.brand.SpuCategoryVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SkuVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuStateVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.biz.goods.rpc.model.spu.SkuQuery;
import com.newzkl.platform.base.biz.market.domain.market.MarketDomain;
import com.newzkl.platform.base.biz.market.domain.relation.GoodsRelationDomain;
import com.newzkl.platform.base.biz.market.model.query.relation.GoodsListPageQuery;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.ApiCategoryVO;
import com.newzkl.platform.base.common.ddd.facade.ApiChannelSpuRelationVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSkuSaleAttributeVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSkuVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuAttributeVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuDetailVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuStateVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuVO;
import com.newzkl.platform.base.common.ddd.facade.MarketRpcVO;
import com.newzkl.platform.base.common.ddd.facade.SelectListApiReq;
import com.newzkl.platform.base.common.ddd.facade.SpuQuery;
import com.newzkl.platform.base.common.ddd.model.enums.goods.GoodsRelationEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.plugin.openapi.domain.adapt.api.GoodsApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品跨域出站端口实现
 *
 * <p>照 biz-order {@code OrderApiImpl} 范式: 已迁能力真调 Base biz-goods
 * {@code SpuService}/{@code SpuCategoryService} 与 biz-market
 * {@code GoodsRelationDomain}/{@code MarketDomain}。biz-market 无 application/facade
 * 层, 直注 domain service。</p>
 *
 * @author KC
 */
@Component("openApiGoodsApi")
@RequiredArgsConstructor
public class GoodsApiImpl implements GoodsApi {

    private final SpuService spuService;
    private final SpuDomain spuDomain;
    private final SpuRepository spuRepository;
    private final SpuCategoryService spuCategoryService;
    private final GoodsRelationDomain goodsRelationDomain;
    private final MarketDomain marketDomain;

    @Override
    public Page<ApiChannelSpuRelationVO> selectList(Long accountId, SelectListApiReq req) {
        GoodsListPageQuery query = new GoodsListPageQuery();
        query.setUserId(accountId);
        query.setRelationType(GoodsRelationEnum.GoodsRelation.SELECT_GOODS.getCode());
        query.setGoodsIdList(req.getSpuIdList());
        query.setGoodsName(req.getSpuName());
        query.setSpuState(req.getSpuState());
        query.setCategoryId(req.getCategoryId());
        query.setMarketId(req.getMarketId());
        query.setPageNo(req.getPageNo());
        query.setPageSize(req.getPageSize());
        // sortField(0 销量/1 金额) 白名单映射列名; sortMode(0 升/1 降) 按下标一一对应
        List<Integer> sortField = req.getSortField();
        List<Integer> sortMode = req.getSortMode();
        if (CollUtil.isNotEmpty(sortField)) {
            for (int i = 0; i < sortField.size(); i++) {
                GoodsRelationEnum.Field field = GoodsRelationEnum.Field.getByCode(sortField.get(i));
                if (field == null) {
                    continue;
                }
                boolean isDesc = sortMode != null && i < sortMode.size()
                        && Integer.valueOf(1).equals(sortMode.get(i));
                query.addSortField(field.getValue(), isDesc);
            }
        }
        return goodsRelationDomain.channelSpuRelationList(query);
    }

    @Override
    public List<ApiSpuVO> spuList(Long accountId, List<Long> spuIdList) {
        SpuQuery spuQuery = new SpuQuery();
        spuQuery.setIdList(spuIdList);
        List<SpuVO> spuVOList = spuDomain.listSelect(spuQuery);
        return TransferUtils.transfers(spuVOList, this::toApiSpuVO);
    }

    @Override
    public List<ApiSkuVO> skuList(Long accountId, List<Long> spuIdList) {
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.setSpuIdList(spuIdList);
        List<SkuVO> skuVOList = spuDomain.skuVOList(skuQuery);
        return TransferUtils.transfers(skuVOList, this::toApiSkuVO);
    }

    @Override
    public ApiSpuDetailVO spuDetail(Long accountId, Long spuId) {
        SpuVO spuVO = spuService.spuVO(spuId, false);
        ApiSpuDetailVO apiSpuDetailVO = new ApiSpuDetailVO();
        apiSpuDetailVO.setSpu(toApiSpuVO(spuVO));
        apiSpuDetailVO.setSkuList(TransferUtils.transfers(spuVO.getSkuList(), this::toApiSkuVO));
        return apiSpuDetailVO;
    }

    @Override
    public List<ApiSpuStateVO> spuSaleState(Long accountId, List<Long> spuIdList) {
        SpuQuery spuQuery = new SpuQuery();
        spuQuery.setIdList(spuIdList);
        List<SpuStateVO> spuStateVOList = spuRepository.spuList(spuQuery, SpuStateVO.class);
        return TransferUtils.transfers(spuStateVOList, (SpuStateVO spuStateVO) -> {
            ApiSpuStateVO apiSpuStateVO = new ApiSpuStateVO();
            apiSpuStateVO.setSpuId(spuStateVO.getId());
            apiSpuStateVO.setSaleState(toSaleState(spuStateVO.getState()));
            return apiSpuStateVO;
        });
    }

    @Override
    public List<ApiCategoryVO> categoryList(Long accountId, Long pid) {
        SpuCategoryQuery query = new SpuCategoryQuery();
        query.setAccountId(accountId);
        query.setPid(pid);
        List<SpuCategoryVO> list = spuCategoryService.categoryList(query);
        return TransferUtils.transfers(list, ApiCategoryVO.class);
    }

    @Override
    public void updateMarketGoodsLabel(Long accountId, Long goodsId, String productLabel) {
        goodsRelationDomain.updateMarketGoodsLabel(accountId, goodsId, productLabel);
    }

    @Override
    public List<MarketRpcVO> queryAccountBindMarket(Long accountId) {
        return marketDomain.queryAccountBindMarket(accountId);
    }
    
    /**
     * SPU 状态映射为对外售卖状态
     *
     * @param state SPU 状态码
     * @return 1 上架, 0 下架
     */
    private CommonEnum.YesOrNo toSaleState(SpuEnum.State state) {
        return SpuEnum.State.SALE == state
                ? CommonEnum.YesOrNo.YES
                : CommonEnum.YesOrNo.NO;
    }
    
    /**
     * SPU 视图对象转对外 API 视图对象
     *
     * @param spuVO SPU 视图对象
     * @return 对外 SPU 视图对象
     */
    private ApiSpuVO toApiSpuVO(SpuVO spuVO) {
        ApiSpuVO apiSpuVO = TransferUtils.transfer(spuVO, ApiSpuVO::new, (s, v) -> {
            v.setUnitPrice(s.getUnitPrice());
            v.setSupplierPriceBegan(s.getExpand().getSupplierPriceBegan());
        });
        apiSpuVO.setSaleAttributeList(TransferUtils.transfers(spuVO.getSpuSaleAttributeList(), ApiSpuAttributeVO.class));
        apiSpuVO.setParamAttributeList(TransferUtils.transfers(spuVO.getSpuParamAttributeList(), ApiSpuAttributeVO.class));
        apiSpuVO.setSaleState(toSaleState(spuVO.getState()));
        return apiSpuVO;
    }
    
    /**
     * SKU 视图对象转对外 API 视图对象
     *
     * @param skuVO SKU 视图对象
     * @return 对外 SKU 视图对象
     */
    private ApiSkuVO toApiSkuVO(SkuVO skuVO) {
        // 对外 RPC 契约保持分 Integer, Money 字段显式降为分 (Hutool 不做 Money↔Integer 转换)
        ApiSkuVO apiSkuVO = TransferUtils.transfer(skuVO, ApiSkuVO::new, (s, v) -> {
            v.setMarketPrice(s.getMarketPrice());
            v.setSalePrice(s.getSalePrice());
            v.setSupplyPrice(s.getSupplyPrice());
            v.setUnitPrice(s.getUnitPrice());
        });
        apiSkuVO.setSaleAttribute(TransferUtils.transfers(skuVO.getSaleAttribute(), ApiSkuSaleAttributeVO.class));
        return apiSkuVO;
    }
}
