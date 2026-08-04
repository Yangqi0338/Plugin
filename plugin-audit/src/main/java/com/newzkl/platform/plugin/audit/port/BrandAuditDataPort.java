package com.newzkl.platform.plugin.audit.port;

import java.util.List;

/**
 * 品牌申请审批数据出站端口
 *
 * <p>品牌申请审批数据读写, Base 当前无 AuditDataBrand 一族, adapter 实现按缺基建处理</p>
 *
 * @author KC
 */
public interface BrandAuditDataPort {

    /**
     * 查品牌申请审批数据详情
     *
     * @param flowId 审批流主键
     * @return 品牌申请审批数据 JSON
     */
    String detail(Long flowId);

    /**
     * 分页查品牌申请审批数据
     *
     * @param queryJson 查询条件 JSON
     * @return 分页结果 JSON
     */
    String pageJson(String queryJson);

    /**
     * 保存品牌申请审批数据
     *
     * @param dataVoJson 品牌申请审批数据 JSON
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
