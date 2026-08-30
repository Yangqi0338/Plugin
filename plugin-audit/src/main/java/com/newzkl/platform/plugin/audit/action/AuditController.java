package com.newzkl.platform.plugin.audit.action;

import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.audit.action.cmd.AuditExecuteCommand;
import com.newzkl.platform.plugin.audit.action.cmd.AuditRefuseExecuteCommand;
import com.newzkl.platform.plugin.audit.port.AdminAccountPort;
import com.newzkl.platform.plugin.audit.workflow.constant.AuditEnum;
import com.newzkl.platform.plugin.audit.workflow.model.AuditAccountView;
import com.newzkl.platform.plugin.audit.workflow.strategy.WorkflowFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台-审批控制器
 *
 * <p>审批通过/拒绝/终止/详情与 6 类审批分页, 按 templateType 分派策略</p>
 *
 * @author KC
 */
@RestController("auditPluginAuditController")
@RequestMapping("/admin/audit")
@RequiredArgsConstructor
public class AuditController {

    private final WorkflowFactory workflowFactory;
    private final AdminAccountPort adminAccountPort;

    /**
     * 审批通过
     *
     * @param command 审批执行命令
     * @return 操作结果
     */
    // TODO[auth-defer]: 源 @Limit(role/name/promise/brand/goods/worktable_audit, set) 待入口 starter 鉴权切面接入
    @PostMapping("/pass")
    public PlatformResult<String> pass(@RequestBody AuditExecuteCommand command) {
        AuditAccountView account = adminAccountPort.currentAccount();
        workflowFactory.getPolicy(command.templateType()).pass(command.flowId(), account, command.editCommand());
        return PlatformResult.success();
    }

    /**
     * 审批拒绝
     *
     * @param command 审批拒绝执行命令
     * @return 操作结果
     */
    // TODO[auth-defer]: 源 @Limit(role/name/promise/brand/goods/worktable_audit, set)
    @PostMapping("/refuse")
    public PlatformResult<String> refuse(@Validated @RequestBody AuditRefuseExecuteCommand command) {
        AuditAccountView account = adminAccountPort.currentAccount();
        workflowFactory.getPolicy(command.templateType()).refuse(command.flowId(), account, command.reason());
        return PlatformResult.success();
    }

    /**
     * 审批终止
     *
     * @param command 审批执行命令
     * @return 操作结果
     */
    // TODO[auth-defer]: 源 @Limit(role/name/promise/brand/goods/worktable_audit, set)
    @PostMapping("/stop")
    public PlatformResult<String> stop(@Validated @RequestBody AuditExecuteCommand command) {
        AuditAccountView account = adminAccountPort.currentAccount();
        workflowFactory.getPolicy(command.templateType()).stop(command.flowId(), account, command.reason());
        return PlatformResult.success();
    }

    /**
     * 审批详情
     *
     * @param templateType 审批模板类型
     * @param flowId 审批流主键
     * @return 审批业务数据
     */
    // TODO[auth-defer]: 源 @Limit(role/name/promise/brand/goods/worktable_audit, get)
    @GetMapping("/detail")
    public PlatformResult<?> detail(@RequestParam("templateType") Long templateType,
                                    @RequestParam("flowId") Long flowId) {
        AuditAccountView account = adminAccountPort.currentAccount();
        return PlatformResult.success(workflowFactory.getPolicy(templateType).detail(flowId, account));
    }

    /**
     * 保证金缴纳审批分页
     *
     * @param pageQuery 分页查询 JSON
     * @return 分页结果
     */
    // TODO[auth-defer]: 源 @Limit(promise_audit, get)
    @PostMapping("/page/promisePay")
    public PlatformResult<?> pagePromisePay(@RequestBody String pageQuery) {
        return PlatformResult.success(pageByType(AuditEnum.TemplateType.PROMISE_FLOW.getCode(), pageQuery));
    }

    /**
     * 工单审批分页
     *
     * @param pageQuery 分页查询 JSON
     * @return 分页结果
     */
    // TODO[auth-defer]: 源 @Limit(worktable_audit + worktable, get)
    @PostMapping("/page/worktable")
    public PlatformResult<?> pageWorktable(@RequestBody String pageQuery) {
        return PlatformResult.success(pageByType(AuditEnum.TemplateType.SPU_WORK_TABLE.getCode(), pageQuery));
    }

    /**
     * 按模板类型分页, 从当前登录人取 accountId/role
     *
     * @param templateType 审批模板类型
     * @param pageQuery 分页查询 JSON
     * @return 分页结果
     */
    private Object pageByType(Long templateType, String pageQuery) {
        AuditAccountView account = adminAccountPort.currentAccount();
        return workflowFactory.getPolicy(templateType).pageJson(account.accountId(), account.identity(), pageQuery);
    }
}
