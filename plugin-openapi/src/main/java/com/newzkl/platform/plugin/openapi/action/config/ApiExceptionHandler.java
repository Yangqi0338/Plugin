package com.newzkl.platform.plugin.openapi.action.config;


import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.ddd.action.config.GlobalExceptionHandler;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



/**
 * @author fang
 */
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends GlobalExceptionHandler {
    /**
     * 异常
     * @param e
     * @return
     */
    @Override
    @ExceptionHandler(Exception.class)
    public PlatformResult<?> handleException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        String message = e.getMessage();
        if(message.contains("JSON parse error")){
            return PlatformResult.fail(BaseErrorCode.PARAM_JSON.getCode(), message);
        }else {
            return PlatformResult.fail(BaseErrorCode.UNKNOWN.getCode(), BaseErrorCode.UNKNOWN.getMessage());
        }
    }
}
