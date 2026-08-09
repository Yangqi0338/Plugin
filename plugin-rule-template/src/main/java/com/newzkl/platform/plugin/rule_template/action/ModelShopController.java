package com.newzkl.platform.plugin.rule_template.action;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.plugin.rule_template.application.service.ModelShopService;
import com.newzkl.platform.base.biz.store.domain.template.service.ModelShopDomain;
import com.newzkl.platform.base.biz.store.model.template.query.ModelShopDataQuery;
import com.newzkl.platform.base.biz.store.model.template.query.ModelShopStorePageQuery;
import com.newzkl.platform.base.biz.store.model.template.req.ApplyModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.req.AuditModelShopReq;
import com.newzkl.platform.base.biz.store.model.template.req.ModelShopUpdateReq;
import com.newzkl.platform.base.biz.store.model.template.req.ModelShopQuery;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopDataRes;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopRes;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopStorePageRes;
import com.newzkl.platform.base.biz.store.model.template.res.ModelShopStyleRes;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 样板店相关接口 (装修模板规则引擎)
 *
 * <p>从 biz-store 迁入, 端点路径与 HTTP 方法逐字保留。</p>
 */
@RestController("ruleTemplateModelShopController")
@RequestMapping("/modelShop")
@RequiredArgsConstructor
@Slf4j
public class ModelShopController {

    private final ModelShopDomain modelShopDomain;
    private final ModelShopService modelShopService;

    @PostMapping("/applyModelShop")
    public PlatformResult<Void> applyModelShop(@RequestBody ApplyModelShopReq req) {
        modelShopDomain.applyModelShop(req);
        return PlatformResult.success();
    }

    @PostMapping("/updateModelShop")
    public PlatformResult<Void> updateModelShop(@RequestBody ModelShopUpdateReq req) {
        modelShopDomain.updateModelShop(req);
        return PlatformResult.success();
    }

    @PostMapping("/auditModelShop")
    public PlatformResult<Void> auditModelShop(@RequestBody AuditModelShopReq req) {
        modelShopDomain.auditModelShop(req);
        return PlatformResult.success();
    }

    @PostMapping("/queryModelShopPage")
    public PlatformResult<Page<ModelShopRes>> queryModelShopPage(@RequestBody ModelShopQuery req) {
        return PlatformResult.success(modelShopDomain.queryModelShopPage(req));
    }

    @PostMapping("/modelShopData")
    public PlatformResult<ModelShopDataRes> modelShopData(@RequestBody ModelShopDataQuery query) {
        return PlatformResult.success(modelShopDomain.modelShopData(query));
    }

    @PostMapping("/modelShopStorePage")
    public PlatformResult<Page<ModelShopStorePageRes>> modelShopStorePage(@RequestBody ModelShopStorePageQuery query) {
        return PlatformResult.success(modelShopDomain.modelShopStorePage(query));
    }

    @GetMapping("/useModelShop")
    public PlatformResult<Void> useModelShop(@RequestParam(required = false) String styleCode) {
        modelShopService.useModelShop(styleCode);
        return PlatformResult.success();
    }

    @GetMapping("/queryModelShopList")
    public PlatformResult<List<ModelShopStyleRes>> queryModelShopList() {
        return PlatformResult.success(modelShopDomain.queryModelShopList());
    }

    @GetMapping("/deleteModelShop")
    public PlatformResult<Void> deleteModelShop(@RequestParam(required = false) Long id) {
        modelShopDomain.deleteModelShop(id);
        return PlatformResult.success();
    }

    @GetMapping("/syncModelShop")
    public PlatformResult<Void> syncModelShop() {
        modelShopDomain.syncModelShop();
        return PlatformResult.success();
    }
}