package com.newzkl.platform.plugin.openapi.application.service;

import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiCategoryVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSkuStockVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSkuVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSpuDetailVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSpuStateVO;
import com.newzkl.platform.base.biz.goods.rpc.model.openapi.ApiSpuVO;
import com.zkl.scm.goods.rpc.model.hdh.Item;
import com.zkl.scm.market.rpc.model.api.ApiChannelSpuRelationVO;
import com.zkl.scm.market.rpc.model.api.SelectListApiReq;
import com.zkl.scm.market.rpc.model.vo.MarketRpcVO;
import com.zkl.scm.rpc.model.ApiPage;

import java.util.List;

/**
 * @author muc_fang
 * @Description: 商品
 * @date 2023/12/159:57
 */
public interface IGoodsService {

    /**
     * 选品列表
     *
     * @param accountId
     * @param selectListApiReq
     * @return
     */
    ApiPage<ApiChannelSpuRelationVO> selectList(Long accountId, SelectListApiReq selectListApiReq);
    /**
     * SPU列表
     *
     * @param accountId
     * @param spuIdList
     * @return
     */
    List<ApiSpuVO> spuList(Long accountId, List<Long> spuIdList);
    /**
     * SKU列表
     *
     * @param accountId
     * @param spuIdList
     * @return
     */
    List<ApiSkuVO> skuList(Long accountId, List<Long> spuIdList);
    /**
     * SPU详情
     *
     * @param accountId
     * @param spuId
     * @return
     */
    ApiSpuDetailVO spuDetail(Long accountId, Long spuId);
    /**
     * SPU销售状态
     *
     * @param accountId
     * @param spuIdList
     * @return
     */
    List<ApiSpuStateVO> spuSaleState(Long accountId, List<Long> spuIdList);
    /**
     * 分类列表
     * @param accountId
     * @param pid
     * @return
     */
    List<ApiCategoryVO> categoryList(Long accountId, Long pid);

    List<ApiSkuStockVO> skuStock(Long accountId, List<Long> skuIdList);

    void hdhEvent(Item detail);

    /**
     * 修改选品商品标签
     */
    void updateMarketGoodsLabel(Long accountId, Long goodsId, String productLabel);

    /**
     * 查询客户绑定市场
     */
    List<MarketRpcVO> queryAccountBindMarket(Long accountId);

}
