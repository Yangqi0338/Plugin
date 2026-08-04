package com.newzkl.platform.plugin.audit.port;

/**
 * SPU 上传审批数据出站端口
 *
 * <p>SPU 上传审批数据读写, 实现落 adapter 层转调 biz-goods AuditDataSpuRepository</p>
 *
 * @author KC
 */
public interface SpuAuditDataPort {

    /**
     * 查 SPU 审批数据详情
     *
     * @param flowId 审批流主键
     * @return SPU 审批数据 JSON
     */
    String detail(Long flowId);

    /**
     * 分页查 SPU 审批数据
     *
     * @param queryJson 查询条件 JSON
     * @return 分页结果 JSON
     */
    String pageJson(String queryJson);

    /**
     * 保存 SPU 审批数据
     *
     * @param dataVoJson SPU 审批数据 JSON
     * @return 审批数据主键
     */
    Long save(String dataVoJson);
}
