package com.newzkl.platform.plugin.hdh;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.goods.domain.spi.ThirdPartyGoodsRecordProcessor;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.hdh.model.Item;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会订货商品回调控制器
 *
 * <p>接收会订货平台推送的商品数据事件, 经 {@link ThirdPartyGoodsRecordProcessor} 派发到
 * 会订货商品同步策略。迁移自 new-scm {@code HuiDingHuoNotifyController.hdhGoodsEvent}。
 * 回调不做鉴权(三方直连), 由 HdhCallBackFilter 验签保证</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/hdh/notify")
public class HdhGoodsNotifyController {

    /**
     * 会订货商品事件回调 推送商品数据 → 派发器同步
     *
     * @param item 会订货商品数据
     * @return 统一响应
     */
    @PostMapping("/hdhGoodsEvent")
    public PlatformResult<Void> hdhGoodsEvent(@Valid @RequestBody Item item) {
        log.info("收到会订货商品事件 outSpuId={}", item == null ? null : item.getId());
        String itemJson = JSONUtil.toJsonStr(item);
        ThirdPartyGoodsRecordProcessor.find().sync(PlatformTypeEnum.HUI_DING_HUO, item == null ? null : item.getId(), itemJson);
        return PlatformResult.success();
    }
}
