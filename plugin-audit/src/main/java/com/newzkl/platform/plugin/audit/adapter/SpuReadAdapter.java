package com.newzkl.platform.plugin.audit.adapter;

import com.alibaba.fastjson2.JSON;
import com.newzkl.platform.base.biz.goods.application.goods.service.goods.GoodsQueryService;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.plugin.audit.port.SpuReadPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * SPU 读取适配器
 *
 * <p>转调 biz-goods GoodsQueryService 取商品快照, 边界处将 SpuVO 序列化为 JSON, 隔离 Base 内部模型</p>
 *
 * @author KC
 */
@Component("auditPluginSpuReadAdapter")
@RequiredArgsConstructor
public class SpuReadAdapter implements SpuReadPort {

    private final GoodsQueryService goodsQueryService;

    @Override
    public String spuInfoJson(Long spuId) {
        SpuVO spuVO = goodsQueryService.spuVO(spuId);
        return spuVO == null ? null : JSON.toJSONString(spuVO);
    }

    @Override
    public String spuName(Long spuId) {
        SpuVO spuVO = goodsQueryService.spuVO(spuId);
        return spuVO == null ? null : spuVO.getName();
    }
}
