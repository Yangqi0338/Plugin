package com.newzkl.platform.plugin.gateway;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.StrUtil;

import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;

/**
 * [Sa-Token 权限认证] 配置类 
 * @author click33
 */
@Configuration
@Slf4j
public class SaTokenConfigure {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 注册 Sa-Token全局过滤器
     * @return
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
            // 拦截地址
            /* 拦截全部path */
            .addInclude("/**")
            // 开放地址 
            .addExclude("/favicon.ico")
            // 鉴权方法：每次访问进入 
            .setAuth(obj -> {
                ServerWebExchange exchange = SaReactorSyncHolder.getContext();
                String path = exchange.getRequest().getURI().getPath();
                //匹配跳过认证地址
                if(isSkipAuth(path)){
                    //越过认证检查
                    log.warn("越过认证检查, path:" + path );
                }else {
                    String token = exchange.getRequest().getHeaders().getFirst(TokenConstants.AUTHENTICATION);
                    if(StrUtil.isEmpty(token)){
                        throw new RuntimeException("未能读取到有效 token");
                    }
                    //登录检查
                    StpUtil.checkLogin();
                    //匹配跳过角色检查地址
                    if(isSkipRole(path)){
                        //越过角色检查
                        log.warn("越过角色检查, path:" + path );
                    }else {
                        // 角色检查
                        String role = exchange.getRequest().getHeaders().getFirst(TokenConstants.ROLE);
                        if (StrUtil.isEmpty(role)) {
                            throw new RuntimeException("服务器异常, 缺少角色");
                        }
                        RoleEnum.CompanyRole companyRole = RoleEnum.CompanyRole.getByCode(Long.parseLong(role));
                        if(companyRole == null){
                            throw new RuntimeException("服务器异常, 角色格式错误");
                        }
                    }
                }
            })
            // 异常处理方法：每次setAuth函数出现异常时进入
            .setError(e -> {
                String message = e.getMessage();
                if (StrUtil.isNotBlank(message)) {
                    if (message.contains("未能读取到有效 token")) {
                        return new SaResult(AccountErrorCode.NO_TOKEN.getCode(), AccountErrorCode.NO_TOKEN.getMessage(), null);
                    } else if (message.contains("token 无效")) {
                        log.warn(message);
                        return new SaResult(AccountErrorCode.EXPIRE.getCode(), AccountErrorCode.EXPIRE.getMessage(), null);
                    }
                }
                return new SaResult(AccountErrorCode.AUTH_ERROR.getCode(), message, null);
            })
            // 前置函数：在每次认证函数之前执行
            .setBeforeAuth(obj -> {
                SaHolder.getResponse()
                    // ---------- 设置跨域响应头 ----------
                    // 允许指定域访问跨域资源
                    .setHeader("Access-Control-Allow-Origin", "*")
                    // 允许所有请求方式
                    .setHeader("Access-Control-Allow-Methods", "*")
                    // 允许的header参数
                    .setHeader("Access-Control-Allow-Headers", "*")
                    // 有效时间
                    .setHeader("Access-Control-Max-Age", "3600")
                ;
                // 如果是预检请求，则立即返回到前端
                SaRouter.match(SaHttpMethod.OPTIONS)
                        .free(r -> System.out.println("--------OPTIONS预检请求，不做处理"))
                        .back();
            })
            ;
    }
    private boolean isSkipAuth(String path) {
        return AuthProperties.skipUrl.stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    private boolean isSkipRole(String path) {
        return AuthProperties.skipRoleUrl.stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }
}
