package com.newzkl.platform.plugin.openapi.domain.adapt.api;


import com.newzkl.platform.base.common.ddd.facade.ApiCategoryVO;
import com.newzkl.platform.base.common.ddd.facade.ApiChannelSpuRelationVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSkuVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuDetailVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuStateVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuVO;
import com.newzkl.platform.base.common.ddd.facade.MarketRpcVO;
import com.newzkl.platform.base.common.ddd.facade.SelectListApiReq;
import com.newzkl.platform.base.common.ddd.model.res.ApiPage;

import java.util.List;

/**
 * 商品跨域出站端口
 *
 * <p>openapi 插件对 biz-goods / biz-market 域的调用收敛于此。对等旧
 * {@code @DubboReference ISpuFacade/IRelationFacade/IMarketFacade}: Base 现为单体,
 * 实现类 {@code GoodsApiImpl} 与领域实现同上下文, 按接口注入即可; 将来拆服务时
 * 只改本端口实现为远程 consumer, application 层零改动</p>
 *
 * @author KC
 */
public interface GoodsApi {

    /**
     * 选品列表
     *
     * @param accountId        渠道商账号主键
     * @param selectListApiReq 选品查询
     * @return 渠道商 SPU 关系分页
     */
    ApiPage<ApiChannelSpuRelationVO> selectList(Long accountId, SelectListApiReq selectListApiReq);

    /**
     * SPU 列表
     *
     * @param accountId 账号主键
     * @param spuIdList SPU 主键列表
     * @return SPU 列表
     */
    List<ApiSpuVO> spuList(Long accountId, List<Long> spuIdList);

    /**
     * SKU 列表
     *
     * @param accountId 账号主键
     * @param spuIdList SPU 主键列表
     * @return SKU 列表
     */
    List<ApiSkuVO> skuList(Long accountId, List<Long> spuIdList);

    /**
     * SPU 详情
     *
     * @param accountId 账号主键
     * @param spuId     SPU 主键
     * @return SPU 详情
     */
    ApiSpuDetailVO spuDetail(Long accountId, Long spuId);

    /**
     * SPU 销售状态
     *
     * @param accountId 账号主键
     * @param spuIdList SPU 主键列表
     * @return SPU 销售状态列表
     */
    List<ApiSpuStateVO> spuSaleState(Long accountId, List<Long> spuIdList);

    /**
     * 分类列表
     *
     * @param accountId 账号主键
     * @param pid       父分类主键
     * @return 分类列表
     */
    List<ApiCategoryVO> categoryList(Long accountId, Long pid);

    /**
     * 修改选品商品标签
     *
     * @param accountId    账号主键
     * @param goodsId      商品主键
     * @param productLabel 商品标签
     */
    void updateMarketGoodsLabel(Long accountId, Long goodsId, String productLabel);

    /**
     * 查询客户绑定市场
     *
     * @param accountId 账号主键
     * @return 绑定市场列表
     */
    List<MarketRpcVO> queryAccountBindMarket(Long accountId);
}
