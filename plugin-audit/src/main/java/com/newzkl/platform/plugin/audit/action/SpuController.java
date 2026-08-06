package com.newzkl.platform.plugin.audit.action;

import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.audit.workflow.service.SpuSubmitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SPU审核控制器
 *
 * <p>供应商提交SPU进入审核流程的对外端点</p>
 *
 * @author KC
 */
@RestController("auditPluginSpuController")
@RequestMapping("/goods/spu")
@RequiredArgsConstructor
public class SpuController {

    private final SpuSubmitService spuSubmitService;

    /**
     * 供应商提交SPU审核
     *
     * <p>源端点迁移自biz-goods SpuController，现转由plugin-audit承接审批流编排。
     * 流程: 读spuVO→扣1商品位→创建SPU_CREATE审批流→回写spuSubmit状态</p>
     *
     * @param spuId SPU主键
     * @param templateId 审批模板主键，默认为SPU_CREATE模板(type=3)
     * @return 审批流主键
     */
    // TODO[auth-defer]: 源 @Limit(role/permission) 待入口starter鉴权切面接入 (#178)
    @PostMapping("spuSubmit")
    public PlatformResult<Long> spuSubmit(@RequestParam Long spuId,
                                           @RequestParam(required = false) Long templateId) {
        // templateId若未传，由SpuSubmitService内部查询SPU_CREATE模板
        // 当前简化实现：要求调用方传入，后续可优化为自动查询
        if (templateId == null) {
            throw new IllegalArgumentException("templateId参数必填 (SPU_CREATE模板主键)");
        }
        Long flowId = spuSubmitService.submit(spuId, templateId);
        return PlatformResult.success(flowId);
    }
}
