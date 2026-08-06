package com.newzkl.platform.plugin.hdh;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.goods.domain.spi.ThirdPartyGoodsStrategy;
import com.newzkl.platform.base.common.ddd.facade.ThirdPartyGoodsResult;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import com.newzkl.platform.plugin.hdh.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 惠订货第三方商品同步策略实现
 *
 * <p>会订货平台 push 商品数据(Item) 本策略反序列化 + 落库记录。
 * new-scm 权威语义为 {@code SpuFacadeImpl.hdhEvent}: 按 outSpuId 查本系统 SPU
 * → {@code buildSpuVOByHdhItem} 构建 → {@code spuDomain.spuUpdate} 回写 spu/sku。
 * 但 Base biz-goods 当前缺 buildSpuVOByHdhItem / spuUpdate / spuPage(均被裁) →
 * 真实 spu 回写属能力缺口(deferred), 本次落「反序列化 + 记录 + 日志」骨架, 不假装端到端。</p>
 */
@Slf4j
@Component
public class HuiDingHuoGoodsStrategy implements ThirdPartyGoodsStrategy {

    @Override
    public ThirdPartyGoodsResult sync(PlatformTypeEnum platformType, String outSpuId, String itemJson) {
        // 反序列化会订货商品数据
        Item item = JSONUtil.toBean(itemJson, Item.class);
        log.info("会订货商品同步 outSpuId={} name={} shelfStatus={}",
                item.getId(), item.getName(), item.getShelfStatus());
        // TODO[deferred] 真实 spu/sku 回写待 Base biz-goods 补 buildSpuVOByHdhItem + spuUpdate 能力面
        //   new-scm: 按 outSpuId 查 SPU → buildSpuVOByHdhItem(item) → spuDomain.spuUpdate(cmd)
        //   当前仅落同步记录, spu 不回写
        String goodsRes = "{\"synced\":false,\"reason\":\"spuUpdate capability gap (deferred)\"}";
        return new HuiDingHuoGoodsResultAdapter(item.getId(), itemJson, goodsRes);
    }

    @Override
    public boolean supports(Object type) {
        return PlatformTypeEnum.HUI_DING_HUO == type;
    }
}
