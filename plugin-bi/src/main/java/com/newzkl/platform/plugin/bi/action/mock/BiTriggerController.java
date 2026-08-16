package com.newzkl.platform.plugin.bi.action.mock;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.bi.domain.service.BiEventWriteService;
import com.newzkl.platform.plugin.bi.model.event.BiTriggerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * BI 模拟触发接口
 *
 * <p>开发/联调阶段手动触发统计事件(会员注册/上链/库存/商品状态/订单等),
 * 直接调 {@link BiEventWriteService} 写宽表, 生产环境由真实 MQ 事件替代。</p>
 */
@RestController("biTriggerController")
@RequestMapping("/bi/trigger")
@RequiredArgsConstructor
public class BiTriggerController {

    private final BiEventWriteService biEventWriteService;

    /** 会员注册 */
    @PostMapping("/memberRegister")
    public PlatformResult<String> memberRegister(@RequestBody BiTriggerEvent event) {
        biEventWriteService.onMemberRegister(CommonEnum.Client.ADMIN, event.getMemberId(), event.getLevel());
        return PlatformResult.success("ok");
    }

    /** 上链存证 */
    @PostMapping("/evidenceOnChain")
    public PlatformResult<String> evidenceOnChain(@RequestBody BiTriggerEvent event) {
        biEventWriteService.onEvidenceOnChain(CommonEnum.Client.ADMIN, event.getEvidenceId(), event.getUserId());
        return PlatformResult.success("ok");
    }

    /** 库存变更 */
    @PostMapping("/inventoryChange")
    public PlatformResult<String> inventoryChange(@RequestBody BiTriggerEvent event) {
        biEventWriteService.onInventoryChange(CommonEnum.Client.ADMIN, event.getGoodsId(), event.getStoreId(),
                event.getAmount(), event.getStatus());
        return PlatformResult.success("ok");
    }

    /** 商品上架 */
    @PostMapping("/goodsOnShelf")
    public PlatformResult<String> goodsOnShelf() {
        biEventWriteService.onGoodsStatus(CommonEnum.Client.ADMIN, "ON_SHELF");
        return PlatformResult.success("ok");
    }

    /** 商品下架 */
    @PostMapping("/goodsOffShelf")
    public PlatformResult<String> goodsOffShelf() {
        biEventWriteService.onGoodsStatus(CommonEnum.Client.ADMIN, "OFF_SHELF");
        return PlatformResult.success("ok");
    }

    /** 订单创建 */
    @PostMapping("/orderCreate")
    public PlatformResult<String> orderCreate(@RequestBody BiTriggerEvent event) {
        biEventWriteService.onOrderCreate(CommonEnum.Client.ADMIN, event.getUserId());
        return PlatformResult.success("ok");
    }

    /** 订单支付(金额模拟) */
    @PostMapping("/orderPay")
    public PlatformResult<String> orderPay(@RequestBody BiTriggerEvent event) {
        biEventWriteService.onGoodsPaySuccess(CommonEnum.Client.ADMIN, event.getUserId(),
                event.getStoreId(), event.getGoodsId(), event.getAmount());
        return PlatformResult.success("ok");
    }
}
