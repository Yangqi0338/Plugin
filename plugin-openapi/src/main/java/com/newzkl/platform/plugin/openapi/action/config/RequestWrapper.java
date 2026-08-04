package com.newzkl.platform.plugin.openapi.action.config;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author niu
 * @description: s
 * @date 2023/5/12 19:16
 */
@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {

    private String body;
    public RequestWrapper(HttpServletRequest request) {
        /**
         * 由于继承了HttpServletRequestWrapper，HttpServletRequestWrapper又继承了ServletRequestWrapper，ServletRequestWrapper
         * 中有一个private ServletRequest request;也就是将原来的request做了一个备份，具体读到的数据放在body中
         */
        super(request);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
            }
        } catch (Exception ex) {
            log.error("过滤器request请求包装时出现异常", ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    log.error("过滤器request请求包装关闭流出现异常", e);
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException e) {
                    log.error("过滤器request请求包装关闭流出现异常", e);
                }
            }
        }
        body = stringBuilder.toString();
        log.info("过滤器request请求包装结果为：" + body);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }
            @Override
            public boolean isReady() {
                return false;
            }
            @Override
            public void setReadListener(ReadListener readListener) {
            }
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;

    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public void setBody(String body) {
        this.body = body;
    }
    public String getBody() {
        return this.body;
    }
}
