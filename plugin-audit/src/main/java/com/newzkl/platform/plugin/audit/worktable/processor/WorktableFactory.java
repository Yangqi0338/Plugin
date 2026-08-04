package com.newzkl.platform.plugin.audit.worktable.processor;

import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 工单处理器工厂
 *
 * <p>启动时把容器内所有 WorktableProcessorSupport 按 support() 注册, 按 operateTarget 分派处理器</p>
 *
 * @author KC
 */
@Component("auditPluginWorktableFactory")
public class WorktableFactory implements InitializingBean, ApplicationContextAware {

    private static final Map<Integer, WorktableProcessorSupport> POLICY_MAP = new HashMap<>();

    private ApplicationContext appContext;

    /**
     * 按操作目标取处理器
     *
     * @param type operateTarget 1~5
     * @return 处理器实例, 未注册时抛 PARAM 而非返回 null
     * @throws PlatformException operateTarget 未注册时抛出
     */
    public WorktableProcessorSupport getPolicy(Integer type) {
        WorktableProcessorSupport policy = POLICY_MAP.get(type);
        if (policy == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "未注册的工单处理器: operateTarget=" + type);
        }
        return policy;
    }

    @Override
    public void afterPropertiesSet() {
        appContext.getBeansOfType(WorktableProcessorSupport.class)
                .values()
                .forEach(processor -> POLICY_MAP.put(processor.support(), processor));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }
}
