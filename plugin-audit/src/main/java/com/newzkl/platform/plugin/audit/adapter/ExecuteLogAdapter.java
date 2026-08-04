package com.newzkl.platform.plugin.audit.adapter;

import com.newzkl.platform.base.biz.goods.domain.spu.service.ExecuteLogDomain;
import com.newzkl.platform.plugin.audit.port.ExecuteLogPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 操作日志适配器
 *
 * <p>转调 biz-goods ExecuteLogDomain 记录审批执行前后快照</p>
 *
 * @author KC
 */
@Component("auditPluginExecuteLogAdapter")
@RequiredArgsConstructor
public class ExecuteLogAdapter implements ExecuteLogPort {

    private final ExecuteLogDomain executeLogDomain;

    @Override
    public Long save(Integer type, Long targetId, String userName, String before, String after) {
        return executeLogDomain.executeLogSave(type, targetId, userName, before, after);
    }
}
