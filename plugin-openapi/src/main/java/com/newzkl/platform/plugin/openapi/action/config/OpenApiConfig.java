package com.newzkl.platform.plugin.openapi.action.config;

import com.newzkl.platform.plugin.openapi.domain.DeveloperApi;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperAuthVO;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperAuthVOBuilder;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperRes;
import com.newzkl.platform.plugin.openapi.domain.IDeveloperDomain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/1510:13
 */
@Configuration
public class OpenApiConfig {

    private final IDeveloperDomain developerDomain;

    public OpenApiConfig(IDeveloperDomain developerDomain) {
        this.developerDomain = developerDomain;
    }

    @Bean
    public DeveloperApi developerApi(){
        return new DeveloperApi() {
            @Override
            public DeveloperAuthVO getDeveloperSecret(String appId) {
                DeveloperRes developerRes = developerDomain.developerVOByAppId(appId);
                if (developerRes == null) {
                    ThrowsException.exception(BaseErrorCode.PARAM, "appId 错误");
                }
                return DeveloperAuthVOBuilder.builder()
                        .appId(appId)
                        .accountId(developerRes.getAccountId())
                        .secret(developerRes.getSecret())
                        .build();
            }
        };
    }
}
