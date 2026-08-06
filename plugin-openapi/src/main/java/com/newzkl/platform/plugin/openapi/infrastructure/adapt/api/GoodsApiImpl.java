package com.newzkl.platform.plugin.openapi.infrastructure.adapt.api;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuCategoryService;
import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuService;
import com.newzkl.platform.base.biz.market.domain.market.service.MarketDomain;
import com.newzkl.platform.base.biz.market.domain.relation.service.GoodsRelationDomain;
import com.newzkl.platform.base.biz.market.model.query.relation.GoodsListPageQuery;
import com.newzkl.platform.base.common.ddd.facade.ApiCategoryVO;
import com.newzkl.platform.base.common.ddd.facade.ApiChannelSpuRelationVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSkuVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuDetailVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuStateVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuVO;
import com.newzkl.platform.base.common.ddd.facade.MarketRpcVO;
import com.newzkl.platform.base.common.ddd.facade.SelectListApiReq;
import com.newzkl.platform.base.common.ddd.model.enums.goods.GoodsRelationEnum;
import com.newzkl.platform.base.common.ddd.model.res.ApiPage;
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
@Component
@RequiredArgsConstructor
public class GoodsApiImpl implements GoodsApi {

    private final SpuService spuService;
    private final SpuCategoryService spuCategoryService;
    private final GoodsRelationDomain goodsRelationDomain;
    private final MarketDomain marketDomain;

    @Override
    public ApiPage<ApiChannelSpuRelationVO> selectList(Long accountId, SelectListApiReq req) {
        GoodsListPageQuery query = new GoodsListPageQuery();
        query.setUserId(accountId);
        query.setRelationType(GoodsRelationEnum.GoodsRelation.SELECT_GOODS.getRelationType());
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
        Page<ApiChannelSpuRelationVO> page = goodsRelationDomain.channelSpuRelationList(query);
        return ApiPage.of(page.getRecords(), (int) page.getCurrent(), (int) page.getSize(), page.getTotal());
    }

    @Override
    public List<ApiSpuVO> spuList(Long accountId, List<Long> spuIdList) {
        return spuService.apiSpuVOList(accountId, spuIdList);
    }

    @Override
    public List<ApiSkuVO> skuList(Long accountId, List<Long> spuIdList) {
        return spuService.apiSkuVOList(accountId, spuIdList);
    }

    @Override
    public ApiSpuDetailVO spuDetail(Long accountId, Long spuId) {
        return spuService.apiSpuDetail(accountId, spuId);
    }

    @Override
    public List<ApiSpuStateVO> spuSaleState(Long accountId, List<Long> spuIdList) {
        return spuService.apiSpuState(accountId, spuIdList);
    }

    @Override
    public List<ApiCategoryVO> categoryList(Long accountId, Long pid) {
        return spuCategoryService.apiCategoryList(accountId, pid);
    }

    @Override
    public void updateMarketGoodsLabel(Long accountId, Long goodsId, String productLabel) {
        goodsRelationDomain.updateMarketGoodsLabel(accountId, goodsId, productLabel);
    }

    @Override
    public List<MarketRpcVO> queryAccountBindMarket(Long accountId) {
        return marketDomain.queryAccountBindMarket(accountId);
    }
}
