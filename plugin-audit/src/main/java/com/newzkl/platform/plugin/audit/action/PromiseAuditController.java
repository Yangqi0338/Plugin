package com.newzkl.platform.plugin.audit.action;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.action.auth.RoleLimit;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.plugin.audit.action.cmd.PromiseAuditRefuseCommand;
import com.newzkl.platform.plugin.audit.action.cmd.PromiseFlowSubmitCommand;
import com.newzkl.platform.plugin.audit.workflow.model.PromiseFlow;
import com.newzkl.platform.plugin.audit.workflow.model.PromiseFlowQuery;
import com.newzkl.platform.plugin.audit.workflow.service.PromiseFlowDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 保证金审核控制器
 *
 * <p>保证金审核第二线内联模式: 供应商线下打款后填金额、传凭证提交, 平台人工审核。
 * 审核态直接落 promise_flow 主数据, 无审批流引擎、无快照、无 MQ 回调。保证金流水与审核归属 audit 域,
 * 自持实体与 CRUD。保证金账户充值与供应商配置读取经 finance {@code PurseFacade} 跨域调用,
 * 供应商主数据回写由 {@link SupplierClientDomain} 负责。</p>
 *
 * @author KC
 */
@RoleLimit(client = AccountEnum.Client.ADMIN)
@RestController("auditPluginPromiseAuditController")
@RequestMapping("/audit/promise")
@RequiredArgsConstructor
public class PromiseAuditController {

    private final PromiseFlowDomain promiseFlowDomain;
    private final SupplierClientDomain supplierClientDomain;

    /**
     * 供应商提交保证金流水单
     *
     * <p>供应商填金额 + 上传支付凭证提交, 非审核动作。accountId 取登录态, 不信前端。
     * 读供应商全局配置决定是否跳过审核: 跳过则建单即通过并直接充值</p>
     *
     * @param command 保证金流水提交命令
     * @return 保证金流水单主键
     */
    @RoleLimit(client = AccountEnum.Client.SUPPLIER)
    @PostMapping("/submit")
    public PlatformResult<Long> submit(@Validated @RequestBody PromiseFlowSubmitCommand command) {
        PromiseFlow promiseFlow = TransferUtils.transfer(command, PromiseFlow::new);
        promiseFlow.setAccountId(SecurityUtils.getAccountId());
        return PlatformResult.success(promiseFlowDomain.submitPromiseFlow(promiseFlow));
    }

    /**
     * 保证金审核列表
     *
     * <p>审核列表排除未提交态: auditState 去 CUSTOM, 仅列待审核/通过/未通过</p>
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<Page<PromiseFlow>> page(@RequestBody PromiseFlowQuery query) {
        query.setAuditStateList(List.of(AuditEnum.State.AUDITING, AuditEnum.State.SUCCESS, AuditEnum.State.FAIL));
        return PlatformResult.success(promiseFlowDomain.promiseFlowPage(query));
    }

    /**
     * 保证金审核详情
     *
     * @param promiseFlowId 保证金流水单主键
     * @return 保证金流水详情
     */
    @GetMapping("/detail")
    public PlatformResult<PromiseFlow> detail(@RequestParam("promiseFlowId") Long promiseFlowId) {
        return PlatformResult.success(promiseFlowDomain.promiseFlow(promiseFlowId));
    }

    /**
     * 保证金审核通过
     *
     * <p>跨域编排: 先取流水单拿供应商ID, 置流水单通过并给保证金账户充值 (finance),
     * 回写供应商主数据 (account, 置已入驻/保证金已缴/写实缴金额)。同事务保证跨域一致</p>
     *
     * @param promiseFlowId 保证金流水单主键
     * @return 操作结果
     */
    @PostMapping("/pass")
    @Transactional(rollbackFor = Exception.class)
    public PlatformResult<String> pass(@RequestParam("promiseFlowId") Long promiseFlowId) {
        PromiseFlow flow = promiseFlowDomain.promiseFlow(promiseFlowId);
        Money amount = promiseFlowDomain.promisePass(promiseFlowId);
        supplierClientDomain.promisePayAuditSuccess(flow.getAccountId(), amount);
        return PlatformResult.success();
    }

    /**
     * 保证金审核拒绝
     *
     * <p>跨域编排: 置流水单未通过并记拒绝原因 (finance), 回写供应商保证金审核态为未通过 (account)</p>
     *
     * @param command 保证金审核拒绝命令
     * @return 操作结果
     */
    @PostMapping("/refuse")
    @Transactional(rollbackFor = Exception.class)
    public PlatformResult<String> refuse(@Validated @RequestBody PromiseAuditRefuseCommand command) {
        PromiseFlow flow = promiseFlowDomain.promiseFlow(command.promiseFlowId());
        promiseFlowDomain.promiseRefuse(command.promiseFlowId(), command.reason());
        supplierClientDomain.promisePayAuditFail(flow.getAccountId(), command.reason());
        return PlatformResult.success();
    }
}
