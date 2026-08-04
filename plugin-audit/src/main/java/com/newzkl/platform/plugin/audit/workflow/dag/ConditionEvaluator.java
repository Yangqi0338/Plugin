package com.newzkl.platform.plugin.audit.workflow.dag;

import java.util.Map;

/**
 * 审批条件节点求值器
 *
 * @author KC
 */
public interface ConditionEvaluator {

    /**
     * 求值条件表达式
     *
     * @param expression 表达式, 取自 audit_template 节点参数
     * @param context 上下文键值, 由策略 getContextParams 产出
     * @return 求值结果, Boolean 或 Number
     */
    Object evaluate(String expression, Map<String, String> context);
}
