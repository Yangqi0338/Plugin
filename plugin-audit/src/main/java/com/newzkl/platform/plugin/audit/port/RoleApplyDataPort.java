package com.newzkl.platform.plugin.audit.port;

import java.util.List;

/**
 * 角色申请审批数据出站端口
 *
 * <p>角色申请审批数据读写, 实现落 adapter 层转调 biz-account AuditDataRoleApply 一族</p>
 *
 * @author KC
 */
public interface RoleApplyDataPort {

    /**
     * 查角色申请审批数据详情
     *
     * @param flowId 审批流主键
     * @return 角色申请审批数据 JSON
     */
    String detail(Long flowId);

    /**
     * 分页查角色申请审批数据
     *
     * @param queryJson 查询条件 JSON
     * @return 分页结果 JSON
     */
    String pageJson(String queryJson);

    /**
     * 保存角色申请审批数据
     *
     * @param dataVoJson 角色申请审批数据 JSON
     * @return 审批数据主键
     */
    Long save(String dataVoJson);

    /**
     * 查同组旧审批流主键, 用于将旧申请置为非最新
     *
     * @param accountId 账号主键
     * @param roleId 角色主键
     * @return 旧审批流主键列表
     */
    List<Long> oldFlowIds(Long accountId, Long roleId);
}
