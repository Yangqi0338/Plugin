package com.newzkl.platform.plugin.audit.workflow.dag;

import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 受限条件求值器
 *
 * <p>替代 JDK21 已移除的 Nashorn 与拿不到引擎的 GraalJS, 仅支持审批模板实际用到的极简布尔文法</p>
 *
 * <p>文法: expr = term (('&&' | '||') term)*; term = operand op operand | 'true' | 'false';
 * operand = context["key"] | 'literal' | number; op = == != &gt; &gt;= &lt; &lt;=</p>
 *
 * <p>表达式来自 DB audit_template 节点参数, 属运维配置面, 故加白名单文法校验加长度上限, 禁反射与 IO 语法</p>
 *
 * @author KC
 */
@Component("auditPluginConditionEvaluator")
public class SimpleConditionEvaluator implements ConditionEvaluator {

    /** 表达式最大长度 */
    private static final int MAX_LENGTH = 200;

    /** 合法 context key 文法 */
    private static final Pattern KEY_PATTERN = Pattern.compile("^[A-Za-z0-9_]{1,32}$");

    /** 禁用 token, 出现即拒绝, 防脚本注入 */
    private static final String[] FORBIDDEN = {"(", ")", ".", ";", "new", "class", "import", "System"};

    @Override
    public Object evaluate(String expression, Map<String, String> context) {
        if (expression == null || expression.length() > MAX_LENGTH) {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
        String expr = expression.trim();
        for (String token : FORBIDDEN) {
            if (expr.contains(token)) {
                throw new PlatformException(BaseErrorCode.PARAM);
            }
        }
        return evalOr(expr, context);
    }

    /**
     * 求值 || 分级
     *
     * @param expr 表达式片段
     * @param context 上下文
     * @return 布尔结果
     */
    private boolean evalOr(String expr, Map<String, String> context) {
        int idx = findOperator(expr, "||");
        if (idx >= 0) {
            return evalOr(expr.substring(0, idx), context)
                    || evalOr(expr.substring(idx + 2), context);
        }
        return evalAnd(expr, context);
    }

    /**
     * 求值 && 分级
     *
     * @param expr 表达式片段
     * @param context 上下文
     * @return 布尔结果
     */
    private boolean evalAnd(String expr, Map<String, String> context) {
        int idx = findOperator(expr, "&&");
        if (idx >= 0) {
            return evalAnd(expr.substring(0, idx), context)
                    && evalAnd(expr.substring(idx + 2), context);
        }
        return evalTerm(expr.trim(), context);
    }

    /**
     * 求值单个比较项
     *
     * @param term 比较项片段
     * @param context 上下文
     * @return 布尔结果
     * @throws PlatformException 文法不合法时抛出
     */
    private boolean evalTerm(String term, Map<String, String> context) {
        if ("true".equals(term)) {
            return true;
        }
        if ("false".equals(term)) {
            return false;
        }
        String[] ops = {"==", "!=", ">=", "<=", ">", "<"};
        for (String op : ops) {
            int idx = term.indexOf(op);
            if (idx >= 0) {
                String left = resolve(term.substring(0, idx).trim(), context);
                String right = resolve(term.substring(idx + op.length()).trim(), context);
                return compare(left, right, op);
            }
        }
        throw new PlatformException(BaseErrorCode.PARAM);
    }

    /**
     * 解析操作数, 支持 context["key"] 取值, 字面量与数字
     *
     * @param operand 操作数片段
     * @param context 上下文
     * @return 解析后的值
     * @throws PlatformException 文法不合法时抛出
     */
    private String resolve(String operand, Map<String, String> context) {
        if (operand.startsWith("context[\"") && operand.endsWith("\"]")) {
            String key = operand.substring("context[\"".length(), operand.length() - 2);
            if (!KEY_PATTERN.matcher(key).matches()) {
                throw new PlatformException(BaseErrorCode.PARAM);
            }
            return context == null ? null : context.get(key);
        }
        if (operand.length() >= 2 && operand.startsWith("'") && operand.endsWith("'")) {
            return operand.substring(1, operand.length() - 1);
        }
        if (operand.matches("^-?\\d+(\\.\\d+)?$")) {
            return operand;
        }
        throw new PlatformException(BaseErrorCode.PARAM);
    }

    /**
     * 比较两个值
     *
     * @param left 左值
     * @param right 右值
     * @param op 比较运算符
     * @return 比较结果
     * @throws PlatformException 数字比较遇非数字时抛出
     */
    private boolean compare(String left, String right, String op) {
        if ("==".equals(op)) {
            return left == null ? right == null : left.equals(right);
        }
        if ("!=".equals(op)) {
            return left == null ? right != null : !left.equals(right);
        }
        double l;
        double r;
        try {
            l = Double.parseDouble(left);
            r = Double.parseDouble(right);
        } catch (NumberFormatException e) {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
        return switch (op) {
            case ">" -> l > r;
            case ">=" -> l >= r;
            case "<" -> l < r;
            case "<=" -> l <= r;
            default -> throw new PlatformException(BaseErrorCode.PARAM);
        };
    }

    /**
     * 在括号外层查逻辑运算符位置, 本文法禁括号, 直接取首个匹配
     *
     * @param expr 表达式
     * @param operator 逻辑运算符
     * @return 位置下标, 未命中返回 -1
     */
    private int findOperator(String expr, String operator) {
        return expr.indexOf(operator);
    }
}
