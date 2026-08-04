package com.newzkl.platform.plugin.openapi.action.config;

import com.zkl.scm.developer.DeveloperApi;
import com.zkl.scm.developer.model.DeveloperAuthVO;
import com.zkl.scm.model.exception.BaseErrorCode;
import com.zkl.scm.model.exception.ThrowsException;
import com.zkl.scm.openapi.domain.developer.model.vo.DeveloperVO;
import com.zkl.scm.openapi.domain.developer.service.IDeveloperDomain;
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
                DeveloperVO developerVO = developerDomain.developerVOByAppId(appId);
                if(developerVO == null){
                    ThrowsException.exception(BaseErrorCode.PARAM, "appId 错误");
                }
                DeveloperAuthVO developerAuthVO = new DeveloperAuthVO();
                developerAuthVO.setAppId(appId);
                developerAuthVO.setAccountId(developerVO.getAccountId());
                developerAuthVO.setSecret(developerVO.getSecret());
                return developerAuthVO;
            }
        };
    }
}
