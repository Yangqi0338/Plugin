package com.newzkl.platform.plugin.audit.model.query;

import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 保证金流水查询
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PromiseFlowQuery extends BizPageQuery {
    /**
     * 审核状态
     */
    private AuditEnum.State auditState;
    /**
     * 审核状态列表 (多值 in 查, 审核列表排除待提交态)
     */
    private List<AuditEnum.State> auditStateList;
}
