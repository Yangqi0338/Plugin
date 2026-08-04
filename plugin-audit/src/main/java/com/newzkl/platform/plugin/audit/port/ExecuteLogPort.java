package com.newzkl.platform.plugin.audit.port;

/**
 * 操作日志出站端口
 *
 * <p>记录审批执行前后快照, 实现落 adapter 层转调 biz-goods ExecuteLogDomain</p>
 *
 * @author KC
 */
public interface ExecuteLogPort {

    /**
     * 保存操作日志
     *
     * @param type 日志类型
     * @param targetId 目标主键
     * @param userName 操作人名称
     * @param before 变更前快照 JSON
     * @param after 变更后快照 JSON
     * @return 日志主键
     */
    Long save(Integer type, Long targetId, String userName, String before, String after);
}
