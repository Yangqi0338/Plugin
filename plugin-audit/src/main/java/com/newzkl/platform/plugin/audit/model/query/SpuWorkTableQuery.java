package com.newzkl.platform.plugin.audit.model.query;

import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * SPU 工单审批数据查询
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpuWorkTableQuery extends BizPageQuery {
    /**
     * SPU 主键
     */
    private Long spuId;
    /**
     * SPU 名称
     */
    private String spuName;
    /**
     * 操作目标
     */
    private SpuEnum.OperateTarget operateTarget;
    /**
     * 操作类型
     */
    private SpuEnum.OperateType operateType;
    /**
     * 审核状态列表 (多值 in 查, 审核列表排除待提交态)
     */
    private List<AuditEnum.State> auditStateList;
}
