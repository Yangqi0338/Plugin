package com.newzkl.platform.plugin.audit.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.plugin.audit.workflow.model.PromiseFlow;
import com.newzkl.platform.plugin.audit.workflow.model.PromiseFlowQuery;

/**
 * 保证金流水领域服务
 *
 * <p>保证金审核第二线内联落地: 建单 / 查 / 审核态流转。保证金账户充值与供应商配置读取经
 * finance {@code PurseFacade} 跨域调用, 供应商主数据回写由 controller 编排层负责</p>
 *
 * @author KC
 */
public interface PromiseFlowDomain {

    /**
     * 提交保证金流水单
     *
     * <p>供应商填金额 + 上传凭证提交。读供应商全局配置决定是否跳过审核: 跳过则建单即通过并直接充值;
     * 否则建单为待审核</p>
     *
     * @param promiseFlow 保证金流水
     * @return 流水单主键
     */
    Long submitPromiseFlow(PromiseFlow promiseFlow);

    /**
     * 保证金流水分页
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<PromiseFlow> promiseFlowPage(PromiseFlowQuery query);

    /**
     * 保证金流水详情
     *
     * @param promiseFlowId 流水单主键
     * @return 保证金流水
     */
    PromiseFlow promiseFlow(Long promiseFlowId);

    /**
     * 保证金审核通过
     *
     * <p>置流水单审核态为通过并给供应商保证金账户充值。仅待审核单可通过, 防越态/重复入账。
     * 返回充值金额供编排层回写供应商主数据</p>
     *
     * @param promiseFlowId 流水单主键
     * @return 实缴保证金金额
     */
    Money promisePass(Long promiseFlowId);

    /**
     * 保证金审核拒绝
     *
     * <p>置流水单审核态为未通过并记录拒绝原因。仅待审核单可拒绝</p>
     *
     * @param promiseFlowId 流水单主键
     * @param refuseReason  拒绝原因
     */
    void promiseRefuse(Long promiseFlowId, String refuseReason);
}
