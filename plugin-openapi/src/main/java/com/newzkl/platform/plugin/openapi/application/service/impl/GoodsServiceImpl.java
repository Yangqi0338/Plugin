package com.newzkl.platform.plugin.openapi.application.service.impl;

import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiCategoryVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSkuStockVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSkuVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSpuDetailVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSpuStateVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSpuVO;
import com.zkl.scm.goods.rpc.facade.ISpuFacade;
import com.zkl.scm.goods.rpc.model.hdh.Item;
import com.zkl.scm.market.rpc.facade.IMarketFacade;
import com.zkl.scm.market.rpc.facade.IRelationFacade;
import com.zkl.scm.market.rpc.model.api.ApiChannelSpuRelationVO;
import com.zkl.scm.market.rpc.model.api.SelectListApiReq;
import com.zkl.scm.market.rpc.model.req.QueryAccountBindMarketReq;
import com.zkl.scm.market.rpc.model.vo.MarketRpcVO;
import com.zkl.scm.model.constants.market.GoodsRelationEnum;
import com.zkl.scm.openapi.application.service.IGoodsService;
import com.zkl.scm.rpc.model.ApiPage;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 商品
 * @date 2023/12/159:58
 */
@Service
public class GoodsServiceImpl implements IGoodsService {

    @DubboReference
    private ISpuFacade spuFacade;
    @DubboReference
    private IRelationFacade relationFacade;
    @DubboReference
    private IMarketFacade marketFacade;

    @Override
    public ApiPage<ApiChannelSpuRelationVO> selectList(Long accountId, SelectListApiReq selectListApiReq) {
        return relationFacade.selectList(accountId, selectListApiReq);
    }

    @Override
    public List<ApiSpuVO> spuList(Long accountId, List<Long> spuIdList) {
        return spuFacade.apiSpuVOList(accountId, spuIdList);
    }
    @Override
    public List<ApiSkuVO> skuList(Long accountId, List<Long> spuIdList) {
        return spuFacade.apiSkuVOList(accountId, spuIdList);
    }
    @Override
    public ApiSpuDetailVO spuDetail(Long accountId, Long spuId) {
        return spuFacade.apiSpuDetail(accountId, spuId);
    }

    @Override
    public List<ApiSpuStateVO> spuSaleState(Long accountId, List<Long> spuIdList) {
        return spuFacade.apiSpuState(accountId, spuIdList);
    }

    @Override
    public List<ApiCategoryVO> categoryList(Long accountId, Long pid) {
        return spuFacade.categoryList(accountId, pid);
    }

    @Override
    public List<ApiSkuStockVO> skuStock(Long accountId, List<Long> skuIdList) {
        return spuFacade.skuStock(accountId, skuIdList);
    }

    @Override
    public void hdhEvent(Item detail) {
        spuFacade.hdhEvent(detail);
    }

    @Override
    public void updateMarketGoodsLabel(Long accountId, Long goodsId, String productLabel) {
        relationFacade.updateMarketGoodsLabel(accountId, goodsId, productLabel);
    }

    @Override
    public List<MarketRpcVO> queryAccountBindMarket(Long accountId){
        return marketFacade.queryAccountBindMarket(new QueryAccountBindMarketReq(accountId, GoodsRelationEnum.GoodsRelation.SELECT_GOODS.getRelationType()));
    }
}
