package com.newzkl.platform.plugin.openapi.action.controller;

import com.alibaba.fastjson.JSONObject;
import com.newzkl.platform.base.biz.account.model.req.ChannelBaseSyncReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelCdkUseReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelCodeSyncReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelGoodsSyncReq;
import com.newzkl.platform.base.biz.account.model.req.ChannelOptionSyncReq;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开放平台-渠道商
 * @author muc_fang
 */
@RestController("开放平台-渠道")
@RequestMapping("/api/channel")
@Slf4j
@RequiredArgsConstructor
public class ChannelController {
    
    private final IChannelFacade channelFacade;
    private final IChannelService channelService;

    /**
     * 同步渠道商信息
     */
    @PostMapping("/syncBase")
    public PlatformResult<Void> syncBase(@Validated @RequestBody ChannelBaseSyncReq req) {
        log.info("同步渠道商信息:" + JSONObject.toJSONString(req));
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        channelFacade.sync(accountId, req);
        return PlatformResult.success();
    }
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
    /**
     * 使用兑换码
     */
    @PostMapping("/useCdk")
    public PlatformResult<ThirdUseStoreCdkRes> useCdk(@Validated @RequestBody ChannelCdkUseReq req) {
        Long appId = DeveloperContextUtil.get(Constants.APP_ID, Long.class);
        Integer serviceId = CommonEnum.SystemType.MK.getCode();
        log.info("使用兑换码:" + JSONObject.toJSONString(req));
        return PlatformResult.success(channelService.useCdk(req, serviceId));
    }
    /**
     * 兑换码列表
     */
    @PostMapping("/cdkList")
    public PlatformResult<ApiPage<CdkVO>> cdkList(@Validated @RequestBody CdkApiQueryReq req) {
        Long appId = DeveloperContextUtil.get(Constants.APP_ID, Long.class);
        Integer serviceId = CommonEnum.SystemType.MK.getCode();
        return PlatformResult.success(channelService.cdkList(req, serviceId));
    }
}
