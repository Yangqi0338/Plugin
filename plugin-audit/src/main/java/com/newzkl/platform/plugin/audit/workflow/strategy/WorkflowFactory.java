package com.newzkl.platform.plugin.audit.workflow.strategy;

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
 * 审批策略工厂
 *
 * <p>启动时把容器内所有 WorkflowStrategy 按 support() 注册, 按模板类型分派策略</p>
 *
 * @author KC
 */
@Component("auditPluginWorkflowFactory")
public class WorkflowFactory implements InitializingBean, ApplicationContextAware {

    private static final Map<Long, WorkflowStrategy<?>> POLICY_MAP = new HashMap<>();

    private ApplicationContext appContext;

    /**
     * 按模板类型取策略
     *
     * @param type 审批模板类型
     * @return 策略实例, 未注册时抛 PARAM 而非返回 null
     * @throws PlatformException 模板类型未注册时抛出
     */
    public WorkflowStrategy<?> getPolicy(Long type) {
        WorkflowStrategy<?> policy = POLICY_MAP.get(type);
        if (policy == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "未注册的审批模板类型: " + type);
        }
        return policy;
    }

    @Override
    public void afterPropertiesSet() {
        appContext.getBeansOfType(WorkflowStrategy.class)
                .values()
                .forEach(strategy -> POLICY_MAP.put(strategy.support(), strategy));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }
}
