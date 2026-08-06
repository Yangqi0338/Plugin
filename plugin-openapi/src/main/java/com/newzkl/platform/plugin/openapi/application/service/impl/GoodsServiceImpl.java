package com.newzkl.platform.plugin.openapi.application.service.impl;


import com.newzkl.platform.base.common.ddd.facade.ApiCategoryVO;
import com.newzkl.platform.base.common.ddd.facade.ApiChannelSpuRelationVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSkuVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuDetailVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuStateVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuVO;
import com.newzkl.platform.base.common.ddd.facade.MarketRpcVO;
import com.newzkl.platform.base.common.ddd.facade.SelectListApiReq;
import com.newzkl.platform.base.common.ddd.model.res.ApiPage;
import com.newzkl.platform.plugin.openapi.application.service.IGoodsService;
import com.newzkl.platform.plugin.openapi.domain.adapt.api.GoodsApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品
 *
 * @author muc_fang
 */
@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements IGoodsService {

    private final GoodsApi goodsApi;

    @Override
    public ApiPage<ApiChannelSpuRelationVO> selectList(Long accountId, SelectListApiReq selectListApiReq) {
        return goodsApi.selectList(accountId, selectListApiReq);
    }

    @Override
    public List<ApiSpuVO> spuList(Long accountId, List<Long> spuIdList) {
        return goodsApi.spuList(accountId, spuIdList);
    }

    @Override
    public List<ApiSkuVO> skuList(Long accountId, List<Long> spuIdList) {
        return goodsApi.skuList(accountId, spuIdList);
    }

    @Override
    public ApiSpuDetailVO spuDetail(Long accountId, Long spuId) {
        return goodsApi.spuDetail(accountId, spuId);
    }

    @Override
    public List<ApiSpuStateVO> spuSaleState(Long accountId, List<Long> spuIdList) {
        return goodsApi.spuSaleState(accountId, spuIdList);
    }

    @Override
    public List<ApiCategoryVO> categoryList(Long accountId, Long pid) {
        return goodsApi.categoryList(accountId, pid);
    }

    @Override
    public void updateMarketGoodsLabel(Long accountId, Long goodsId, String productLabel) {
        goodsApi.updateMarketGoodsLabel(accountId, goodsId, productLabel);
    }

    @Override
    public List<MarketRpcVO> queryAccountBindMarket(Long accountId) {
        return goodsApi.queryAccountBindMarket(accountId);
    }
}
