package com.newzkl.platform.plugin.bi.action.channel;

import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.bi.application.BiApplicationService;
import com.newzkl.platform.plugin.bi.model.res.ChannelHomeRes;
import com.newzkl.platform.plugin.bi.model.res.ChannelWeekTradeItemRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 渠道商BI控制器
 */
@RestController("biChannelController")
@RequestMapping("/bi/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final BiApplicationService biAppService;

    /** 渠道商 HOME 总览 */
    @PostMapping("/home")
    public PlatformResult<ChannelHomeRes> home() {
        return PlatformResult.success(biAppService.channelHome());
    }

    /** 渠道商本周交易走势(销售额, 万元) */
    @PostMapping("/weekTrade")
    public PlatformResult<List<ChannelWeekTradeItemRes>> weekTrade() {
        return PlatformResult.success(biAppService.channelWeekTrade());
    }
}
