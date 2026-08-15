package com.newzkl.platform.plugin.channel.config;

import com.newzkl.platform.base.common.ddd.application.spi.IdentityImpl;
import com.newzkl.platform.base.common.ddd.application.spi.demo.IdentityConfigExt;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;

/**
 * 身份配置提供-渠道商实现(演示)。
 *
 * <p>命中渠道商身份, 返回带渠道商标识的配置值, 纯内存零依赖, 用于验证身份 SPI 分发。</p>
 *
 * @author KC
 */
@IdentityImpl(RoleEnum.CompanyRole.CHANNEL)
public class ChannelConfigProvider implements IdentityConfigExt {

    @Override
    public String config(String key) {
        return "channel:" + key;
    }
}
