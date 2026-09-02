package com.newzkl.platform.plugin.audit.action;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.plugin.audit.action.cmd.SaleAttributeAddRequest;
import com.newzkl.platform.plugin.audit.action.cmd.SaleAttributeDeleteRequest;
import com.newzkl.platform.plugin.audit.action.cmd.SkuBaseUpdateRequest;
import com.newzkl.platform.plugin.audit.action.cmd.SpuBaseUpdateRequest;
import com.newzkl.platform.plugin.audit.action.cmd.SpuStateUpdateRequest;
import com.newzkl.platform.plugin.audit.action.cmd.SpuWorkTableExecuteCommand;
import com.newzkl.platform.plugin.audit.action.cmd.SpuWorkTableRefuseCommand;
import com.newzkl.platform.plugin.audit.model.dto.SpuWorkTableDTO;
import com.newzkl.platform.plugin.audit.model.query.SpuWorkTableQuery;
import com.newzkl.platform.plugin.audit.domain.SpuWorkTableDomain;
import com.newzkl.platform.plugin.audit.application.SpuWorkTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品-工单控制器
 *
 * <p>商品变更走工单审批: 提交后落审批数据, 由平台审核后再生效。提交编排与状态流转在 {@link SpuWorkTableDomain},
 * 审批通过的 SPU 主数据真实变更在 {@link SpuWorkTableService}。</p>
 *
 * @author KC
 */
@RestController("auditPluginWorktableController")
@RequestMapping("/goods/worktable")
@RequiredArgsConstructor
public class WorktableController {

    private final SpuWorkTableDomain spuWorkTableDomain;
    private final SpuWorkTableService spuWorkTableService;

    /**
     * 提交 SPU 基础信息修改工单
     *
     * @param request SPU 基础信息修改入参, 含 spuVO.id
     * @return 空结果
     */
    @PostMapping("spuUpdate")
    public PlatformResult<Void> spuUpdate(@Validated @RequestBody SpuBaseUpdateRequest request) {
        spuWorkTableDomain.submitSpuBaseUpdate(request.spuVO());
        return PlatformResult.success();
    }

    /**
     * 提交 SKU 基础信息修改工单
     *
     * @param request SKU 基础信息修改入参, 含 spuId 与 skuVOList
     * @return 空结果
     */
    @PostMapping("skuUpdate")
    public PlatformResult<Void> skuUpdate(@Validated @RequestBody SkuBaseUpdateRequest request) {
        spuWorkTableDomain.submitSkuBaseUpdate(request.spuId(), request.skuVOList());
        return PlatformResult.success();
    }

    /**
     * 提交 SPU 状态修改工单
     *
     * @param request SPU 状态修改入参, 含 spuId 列表与 state
     * @return 空结果
     */
    @PostMapping("spuStateUpdate")
    public PlatformResult<Void> spuStateUpdate(@Validated @RequestBody SpuStateUpdateRequest request) {
        spuWorkTableDomain.submitSpuStateUpdate(request.spuId(), request.enable());
        return PlatformResult.success();
    }

    /**
     * 提交规格删除工单
     *
     * @param request 规格删除入参, 含 spuId 与删除后保留的销售属性列表
     * @return 空结果
     */
    @PostMapping("saleAttributeDelete")
    public PlatformResult<Void> saleAttributeDelete(@Validated @RequestBody SaleAttributeDeleteRequest request) {
        spuWorkTableDomain.submitSaleAttributeDelete(request.spuId(), request.spuSaleAttributeList());
        return PlatformResult.success();
    }

    /**
     * 提交规格新增工单
     *
     * <p>提交前先做笛卡尔积校验, 校验通过再落工单</p>
     *
     * @param request 规格新增入参, 含 spuId 与 skuList 与 spuSaleAttributeList
     * @return 空结果
     */
    @PostMapping("saleAttributeAdd")
    public PlatformResult<Void> saleAttributeAdd(@Validated @RequestBody SaleAttributeAddRequest request) {
        spuWorkTableDomain.submitSaleAttributeAdd(request.spuId(), request.skuList(), request.spuSaleAttributeList());
        return PlatformResult.success();
    }

    /**
     * 工单审批分页
     *
     * @param query 分页查询
     * @return 分页结果
     */
    @PostMapping("page")
    public PlatformResult<Page<SpuWorkTableDTO>> page(@RequestBody SpuWorkTableQuery query) {
        return PlatformResult.success(spuWorkTableDomain.workTablePage(query));
    }

    /**
     * 工单审批详情
     *
     * @param workTableId 工单主键
     * @return 工单审批业务数据
     */
    @GetMapping("detail")
    public PlatformResult<SpuWorkTableDTO> detail(@RequestParam("workTableId") Long workTableId) {
        return PlatformResult.success(spuWorkTableDomain.workTable(workTableId));
    }

    /**
     * 工单审批通过
     *
     * @param command 工单审批执行命令
     * @return 操作结果
     */
    @PostMapping("pass")
    public PlatformResult<String> pass(@RequestBody SpuWorkTableExecuteCommand command) {
        spuWorkTableService.pass(command.workTableId(), command.skuSalePrice());
        return PlatformResult.success();
    }

    /**
     * 工单审批拒绝
     *
     * @param command 工单审批拒绝命令
     * @return 操作结果
     */
    @PostMapping("refuse")
    public PlatformResult<String> refuse(@Validated @RequestBody SpuWorkTableRefuseCommand command) {
        spuWorkTableService.refuse(command.workTableId(), command.reason());
        return PlatformResult.success();
    }
    
        /**
     * 审批终止
     * @return
     */
    @PostMapping("/stop")
    public PlatformResult<String> stop(@Validated @RequestBody SpuWorkTableRefuseCommand command) {
        spuWorkTableDomain.stop(command.workTableId(), command.reason());
        return PlatformResult.success();
    }
}
