package com.newzkl.platform.plugin.operator.config;

import com.newzkl.platform.base.common.ddd.application.spi.IdentityImpl;
import com.newzkl.platform.base.common.ddd.application.spi.demo.IdentityConfigExt;

/**
 * 身份配置提供-运营商实现(演示)。
 *
 * <p>命中运营商身份, 返回带运营商标识的配置值, 纯内存零依赖, 用于验证身份 SPI 分发。</p>
 *
 * @author KC
 */
@IdentityImpl(1004L)
public class OperatorConfigProvider implements IdentityConfigExt {

    @Override
    public String config(String key) {
        return "operator:" + key;
    }
}
