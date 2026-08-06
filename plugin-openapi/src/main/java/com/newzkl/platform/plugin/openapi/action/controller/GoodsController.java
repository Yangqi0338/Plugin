package com.newzkl.platform.plugin.openapi.action.controller;

import cn.hutool.core.date.DateUtil;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.GoldVO;

import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.ddd.facade.ApiCategoryVO;
import com.newzkl.platform.base.common.ddd.facade.ApiChannelSpuRelationVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSkuVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuDetailVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuStateVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuVO;
import com.newzkl.platform.base.common.ddd.facade.MarketRpcVO;
import com.newzkl.platform.base.common.ddd.facade.SelectListApiReq;
import com.newzkl.platform.base.common.ddd.model.enums.market.MarketTypeEnum;
import com.newzkl.platform.base.common.ddd.model.res.ApiPage;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.openapi.action.cmd.GoodsCmd;
import com.newzkl.platform.plugin.openapi.application.service.IGoodsService;
import com.newzkl.platform.plugin.openapi.model.constants.Constants;
import com.newzkl.platform.plugin.openapi.model.util.DeveloperContextUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 开放平台-商品
 * @author muc_fang
 */
@RestController("开放平台-商品")
@RequestMapping("/api/goods")
@Setter(onMethod_ = @Autowired)
@Validated
public class GoodsController {

    private final IGoodsService goodsService;

    public GoodsController(IGoodsService goodsService) {
        this.goodsService = goodsService;
    }

    /**
     * 查询选品列表
     */
    @PostMapping("/selectList")
    public PlatformResult<ApiPage<ApiChannelSpuRelationVO>> selectList(@RequestBody SelectListApiReq selectListApiReq) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        ApiPage<ApiChannelSpuRelationVO> page = goodsService.selectList(accountId, selectListApiReq);

        // 黄金专区填充金价更新时间
        Object goldPriceUpdateTime = RedisUtil.get("GoldRealTimePrice::updateTime");
        page.getList().forEach(x -> {
            if(MarketTypeEnum.GOLD_ZONE.getDesc().equals(x.getMarketType())){
                x.setGoldPriceUpdateTime(Objects.toString(goldPriceUpdateTime, null));
            }
        });
        return PlatformResult.success(page);
    }

    /**
     * 查询SPU列表
     */
    @PostMapping("/spuList")
    public PlatformResult<List<ApiSpuVO>> spuList(@RequestBody GoodsCmd.SpuIdListReq spuIdListReq) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        return PlatformResult.success(goodsService.spuList(accountId, spuIdListReq.getSpuIdList()));
    }
    /**
     * 查询SKU列表
     */
    @PostMapping("/skuList")
    public PlatformResult<List<ApiSkuVO>> skuList(@RequestBody GoodsCmd.SpuIdListReq spuIdsReq) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        return PlatformResult.success(goodsService.skuList(accountId, spuIdsReq.getSpuIdList()));
    }
    /**
     * 查询SPU详情
     */
    @PostMapping("/spuDetail")
    public PlatformResult<ApiSpuDetailVO> spuDetail(@RequestBody GoodsCmd.SpuIdReq spuId) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        return PlatformResult.success(goodsService.spuDetail(accountId, spuId.getSpuId()));
    }
    /**
     * 查询SPU销售状态
     */
    @PostMapping("/spuSaleState")
    public PlatformResult<List<ApiSpuStateVO>> spuSaleState(@RequestBody GoodsCmd.SpuIdListReq spuIdListReq) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        return PlatformResult.success(goodsService.spuSaleState(accountId, spuIdListReq.getSpuIdList()));
    }
    /**
     * 查询分类
     */
    @PostMapping("/categoryList")
    public PlatformResult<List<ApiCategoryVO>> categoryList(@RequestBody GoodsCmd.CategoryListReq categoryListReq) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        return PlatformResult.success(goodsService.categoryList(accountId, categoryListReq.getPid()));
    }
    /**
     * 修改选品商品标签
     */
    @PostMapping("/updateMarketGoodsLabel")
    public PlatformResult<Void> updateMarketGoodsLabel(@RequestBody GoodsCmd.MarketGoodsInfoReq marketGoodsInfoReq) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        goodsService.updateMarketGoodsLabel(accountId, marketGoodsInfoReq.getGoodsId(), marketGoodsInfoReq.getLabel());
        return PlatformResult.success();
    }

    /**
     * 查询专区列表
     */
    @PostMapping("/queryAccountBindMarket")
    public PlatformResult<List<MarketRpcVO>> queryAccountBindMarket() {
        return PlatformResult.success(goodsService.queryAccountBindMarket(DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class)));
    }

    /**
     * 保存黄金实时价格
     */
    @PostMapping("/saveGoldRealTimePrice" )
    public PlatformResult<Void> saveGoldRealTimePrice(@RequestBody GoodsCmd.GoldRealTimePriceReq req) {
        RedisUtil.set("GoldRealTimePrice::realTimePrice",req.getPrice());
        RedisUtil.set("GoldRealTimePrice::updateTime",DateUtil.now());
        return PlatformResult.success();
    }

    /**
     * 获取黄金实时价格
     */
    @PostMapping("/getGoldRealTimePrice" )
    public PlatformResult<GoldVO> saveGoldRealTimePrice() {
        GoldVO goldVO = new GoldVO();
        goldVO.setRealTimePrice(RedisUtil.get("GoldRealTimePrice::realTimePrice"));
        goldVO.setUpdateTime(RedisUtil.get("GoldRealTimePrice::updateTime"));
        return PlatformResult.success(goldVO);
    }
}

