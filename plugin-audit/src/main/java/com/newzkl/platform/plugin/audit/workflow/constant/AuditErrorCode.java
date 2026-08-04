package com.newzkl.platform.plugin.audit.workflow.constant;

import com.newzkl.platform.base.common.core.model.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审批错误码
 *
 * <p>审批引擎专属错误码, 复用 core-model 的 ErrorCode 契约</p>
 *
 * @author KC
 */
@Getter
@AllArgsConstructor
public enum AuditErrorCode implements ErrorCode {

    /** 审批模板配置异常 */
    AUDIT_TEMPLATE_CONFIG_ERROR(999, "审批模板配置异常"),

    /** 审批流关联数据丢失 */
    AUDIT_DATA_LOSE(999, "审批流关联数据丢失"),

    /** 审批已存在 */
    EXIST_AUDIT(999, "审批已存在"),

    /** 审批已完结 */
    AUDIT_FINISHED(999, "审批已完结"),

    /** 审批单不存在 */
    NOT_FOUND(999, "审批单不存在"),

    /** 修改业务数据失败 */
    EDIT_BUSINESS_DATA(999, "修改业务数据失败"),

    /** 流转节点失败 */
    FLOW_CODE_ERROR(999, "流转节点失败");

    private final Integer code;
    private final String message;
}
