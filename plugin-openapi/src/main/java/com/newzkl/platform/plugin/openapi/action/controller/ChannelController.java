package com.newzkl.platform.plugin.openapi.action.controller;

import com.alibaba.fastjson.JSONObject;
import com.newzkl.platform.base.biz.account.model.req.ChannelCodeSyncReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelGoodsSyncReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelOptionSyncReq;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.openapi.application.service.IChannelService;
import com.newzkl.platform.plugin.openapi.model.constants.Constants;
import com.newzkl.platform.plugin.openapi.model.util.DeveloperContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开放平台-渠道商
 *
 * <p>D-30 能力缺口: syncBase (IChannelFacade 未建) / useCdk / cdkList (CDK 4 模型 Base 无)
 * 三端点暂移除, 待 Base 补渠道 facade + CDK 域后回填
 *
 * @author muc_fang
 */
@RestController("openApiChannelController")
@RequestMapping("/api/channel")
@Slf4j
@RequiredArgsConstructor
public class ChannelController {

    private final IChannelService channelService;

    /**
     * 同步商品类别信息
     */
    @PostMapping("/syncGoods")
    public PlatformResult<Void> syncGoods(@RequestBody ChannelGoodsSyncReq req) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        log.info("同步商品类别信息:" + JSONObject.toJSONString(req));
        return PlatformResult.success();
    }

    /**
     * 同步兑换码信息
     */
    @PostMapping("/syncCode")
    public PlatformResult<Void> syncCode(@Validated @RequestBody ChannelCodeSyncReq req) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        channelService.syncCode(req);
        return PlatformResult.success();
    }

    /**
     * 同步期权
     */
    @PostMapping("/syncOption")
    public PlatformResult<Void> syncOption(@Validated @RequestBody ChannelOptionSyncReq req) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        log.info("同步期权:" + JSONObject.toJSONString(req));
        channelService.syncOption(accountId, req);
        return PlatformResult.success();
    }
}
