package com.newzkl.platform.plugin.supplier.config;

import com.newzkl.platform.base.common.ddd.application.spi.IdentityImpl;
import com.newzkl.platform.base.common.ddd.application.spi.demo.IdentityConfigExt;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;

/**
 * 身份配置提供-供应商实现(演示)。
 *
 * <p>命中供应商身份, 返回带供应商标识的配置值, 纯内存零依赖, 用于验证身份 SPI 分发。</p>
 *
 * @author KC
 */
@IdentityImpl(AccountEnum.Identity.SUPPLIER)
public class SupplierConfigProvider implements IdentityConfigExt {

    @Override
    public String config(String key) {
        return "supplier:" + key;
    }
}
