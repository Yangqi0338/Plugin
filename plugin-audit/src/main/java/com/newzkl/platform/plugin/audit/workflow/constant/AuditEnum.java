package com.newzkl.platform.plugin.audit.workflow.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审批枚举
 *
 * <p>承载审批模板类型, 审批状态, 审批动作与开关等审批引擎常量</p>
 *
 * @author KC
 */
public final class AuditEnum {

    /** 审批流终态节点 code */
    public static final String END_STEP_CODE = "end";

    private AuditEnum() {
    }

    /**
     * 审批模板类型, 工厂按此分派策略
     */
    @Getter
    @AllArgsConstructor
    public enum TemplateType {

        /** 保证金缴纳审批模板 */
        PROMISE_FLOW(2L, "保证金缴纳审批模板"),

        /** SPU上传审批模板 */
        SPU_CREATE(3L, "SPU上传审批模板"),

        /** SPU工单审批模板 */
        SPU_WORK_TABLE(5L, "SPU工单审批模板"),
        
        ;

        private final Long code;
        private final String value;
    }

    /**
     * 审批状态
     */
    @Getter
    @AllArgsConstructor
    public enum State {

        /** 待用户提交 */
        CUSTOM(0, "待用户提交"),

        /** 待审核 */
        AUDITING(1, "待审核"),

        /** 通过 */
        SUCCESS(2, "通过"),

        /** 未通过 */
        FAIL(3, "未通过"),

        /** 终止 */
        STOP(4, "终止");

        private final Integer code;
        private final String value;
    }

    /**
     * 审批动作
     */
    @Getter
    @AllArgsConstructor
    public enum Action {

        /** 拒绝 */
        REFUSE(0, "拒绝"),

        /** 通过 */
        PASS(1, "通过");

        private final Integer code;
        private final String value;
    }

    /**
     * 是否最新开关
     */
    @Getter
    @AllArgsConstructor
    public enum Switch {

        /** 否 */
        OFF(0, "否"),

        /** 是 */
        ON(1, "是");

        private final Integer code;
        private final String value;
    }
}
