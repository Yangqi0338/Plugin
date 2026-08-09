package com.newzkl.platform.plugin.bi.action.count;

import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.bi.application.service.IndexService;
import com.newzkl.platform.plugin.bi.model.res.SupplierIndexVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页统计控制器
 */
@RestController("biIndexController")
@RequestMapping("/countSale")
@RequiredArgsConstructor
public class IndexController {

    private final IndexService indexService;

    @PostMapping("supplierIndex")
    public PlatformResult<SupplierIndexVO> supplierIndex() {
        return PlatformResult.success(indexService.supplierIndex());
    }

    @PostMapping("channelIndex")
    public PlatformResult<SupplierIndexVO> channelIndex() {
        return PlatformResult.success(indexService.channelIndex());
    }
}