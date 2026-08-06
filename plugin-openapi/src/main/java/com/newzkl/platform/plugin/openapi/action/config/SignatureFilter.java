package com.newzkl.platform.plugin.openapi.action.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.openapi.model.constants.Constants;
import com.newzkl.platform.plugin.openapi.domain.DeveloperApi;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperAuthVO;
import com.newzkl.platform.plugin.openapi.model.util.DeveloperContextUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author fang
 */
@Slf4j
@Component
public class SignatureFilter implements Filter {

    @Autowired
    private DeveloperApi developerApi;

    LoadingCache<String, DeveloperAuthVO> loadingCache = CacheBuilder.newBuilder()
            .maximumSize(20)
            .expireAfterWrite(7, TimeUnit.DAYS)
            .build(new CacheLoader<String, DeveloperAuthVO>() {
                @Nullable
                @Override
                public DeveloperAuthVO load(@Nullable String key){
                    return developerApi.getDeveloperSecret(key);
                }
            });

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getMethod().equals(HttpMethod.POST.name())) {
            String bodyInfo = "";
            //开发者id
            String appId = request.getHeader(Constants.APP_ID);
            //开发者的sign
            String developerSign = request.getHeader(Constants.SIGN);
            String timestamp = request.getHeader(Constants.TIME_STAMP);
            try {
                if (StrUtil.isBlank(appId)) {
                    failResponse(servletResponse,"appId不能为空");
                    return;
                }
                if (StrUtil.isBlank(developerSign)) {
                    failResponse(servletResponse,"sign不能为空");
                    return;
                }
                if (!validateTimestamp(timestamp)) {
                    failResponse(servletResponse,"已超时");
                    return;
                }
                //开发者的密钥
                String secret = loadingCache.get(appId).getSecret();
                //获取参数Map
                InputStream is= request.getInputStream();
                bodyInfo = IOUtils.toString(is, "utf-8");

                Map<String, Object> params = buildParam(bodyInfo, request);
                //根据不同的请求参数进行验签
                String sign = SignatureUtil.sign(params, secret, "sign");
                if (!sign.equals(developerSign)) {
                    //把返回值输出到客户端
                    failResponse(servletResponse,"sign签名失败");
                    log.error("签名验证失败-appId:{},sign:{},参数：{}", appId, sign, bodyInfo);
                    return;
                } else {
                    //存储账号ID
                    DeveloperContextUtil.set(Constants.ACCOUNT_ID, loadingCache.get(appId).getAccountId());
                    SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, loadingCache.get(appId).getAccountId());
                    SecurityContextHolder.set(TokenConstants.DETAILS_COMPANY_ROLE, RoleEnum.CompanyRole.CHANNEL.getCode());
                }
            } catch (Exception e) {
                log.error("签名验证失败-appId:{},参数：{}", appId, bodyInfo, e);
                failResponse(servletResponse,"sign签名失败");
                return;
            }
            try{
                filterChain.doFilter(servletRequest, servletResponse);
            }finally {
                DeveloperContextUtil.remove();
            }
        } else {
            ThrowsException.exception(BaseErrorCode.REMOTE, "仅支持POST请求");
        }
    }

    private Map<String,Object> buildParam(String bodyInfo,HttpServletRequest request){
        Map<String, Object> params = null;
        if(StrUtil.isEmpty(bodyInfo)){
            params = new HashMap<String, Object>();
        } else {
            params = JSON.parseObject(bodyInfo,Map.class);
        }
        params.put(Constants.APP_ID, request.getHeader(Constants.APP_ID));
        params.put(Constants.SIGN, request.getHeader(Constants.SIGN));
        params.put(Constants.RANDOM_NUMBER, request.getHeader(Constants.RANDOM_NUMBER));
        params.put(Constants.TIME_STAMP, request.getHeader(Constants.TIME_STAMP));
        return params;
    }

    public static boolean validateTimestamp(String timestamp) {
        if (timestamp == null) {
            return false;
        }

        Date date = new Date(Long.parseLong(timestamp));
        return !date.before(new Date(System.currentTimeMillis() - 10000));
    }

    /**
     * 异常响应
     * @param servletResponse
     * @param failMsg
     * @throws IOException
     */
    private void failResponse(ServletResponse servletResponse, String failMsg) throws IOException {
        ServletOutputStream out = servletResponse.getOutputStream();
        out.write(JSON.toJSONString(PlatformResult.fail(failMsg)).getBytes());
        out.flush();
        out.close();
    }
}