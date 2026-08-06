package com.newzkl.platform.plugin.openapi.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.goods.application.goods.service.spu.SpuService;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiCategoryVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiChannelSpuRelationVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSkuStockVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSkuVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSpuDetailVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSpuStateVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSpuVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.MarketRpcVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.SelectListApiReq;
import com.newzkl.platform.base.common.ddd.model.res.ApiPage;
import com.newzkl.platform.plugin.openapi.domain.adapt.api.GoodsApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品跨域出站端口实现
 *
 * <p>照 biz-order {@code SpuApiImpl} 范式: 已迁能力真调 Base biz-goods
 * {@code SpuService}; 未迁能力显式抛 {@code UnsupportedOperationException}
 * 而非静默返回空, 缺口登记 rebuild/docs/planning/deferred-issues.md D-30</p>
 *
 * @author KC
 */
@Component
@RequiredArgsConstructor
public class GoodsApiImpl implements GoodsApi {

    private final SpuService spuService;

    @Override
    public ApiPage<ApiChannelSpuRelationVO> selectList(Long accountId, SelectListApiReq selectListApiReq) {
        throw new UnsupportedOperationException(
                "TODO[capability-gap]: 选品列表待迁; Base GoodsRelationDomain.channelSpuRelationList "
                        + "的 XML 引 ${req.sortSQL} 但 GoodsListPageQuery 无该字段, SelectListApiReq "
                        + "的 sortField/sortMode→sortSQL 映射未迁, 不臆造映射");
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
        throw new UnsupportedOperationException(
                "TODO[capability-gap]: SPU 详情待迁; Base biz-goods SpuService 未迁 apiSpuDetail");
    }

    @Override
    public List<ApiSpuStateVO> spuSaleState(Long accountId, List<Long> spuIdList) {
        return spuService.apiSpuState(accountId, spuIdList);
    }

    @Override
    public List<ApiCategoryVO> categoryList(Long accountId, Long pid) {
        throw new UnsupportedOperationException(
                "TODO[capability-gap]: 分类列表待迁; Base biz-goods SpuCategoryService 对外 api "
                        + "categoryList(accountId,pid) 未迁");
    }

    @Override
    public List<ApiSkuStockVO> skuStock(Long accountId, List<Long> skuIdList) {
        throw new UnsupportedOperationException(
                "TODO[capability-gap]: SKU 库存待迁; Base biz-goods SpuService 未迁 skuStock");
    }

    @Override
    public void updateMarketGoodsLabel(Long accountId, Long goodsId, String productLabel) {
        throw new UnsupportedOperationException(
                "TODO[capability-gap]: 改选品标签待迁; Base GoodsRelationDomain 只收已构造 "
                        + "UpdateGoodsRelationReq{id,goodsInfo}, scm 语义需先按 (accountId,goodsId,"
                        + "SELECT_GOODS) 查 relation 拿 id+goodsInfo 再回填 label, 该 lookup 未迁 Base");
    }

    @Override
    public List<MarketRpcVO> queryAccountBindMarket(Long accountId) {
        throw new UnsupportedOperationException(
                "TODO[capability-gap]: 查绑定市场待迁; Base biz-market 未迁 queryAccountBindMarket");
    }
}
