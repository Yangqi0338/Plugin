package com.newzkl.platform.plugin.bi.action.order;

import com.newzkl.platform.base.biz.order.domain.service.OrderDomain;
import com.newzkl.platform.base.biz.order.model.dto.IndexCountRes;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单-统计
 * @author fang
 */
@RestController
@RequestMapping("/order/count")
@Slf4j
@RequiredArgsConstructor
public class CountController {

    private final OrderDomain orderDomain;

    @PostMapping("indexCount")
    public PlatformResult<IndexCountRes> indexCount(@RequestBody TimeQuery timeQuery) {
        return PlatformResult.success(orderDomain.indexCount(timeQuery));
    }
}