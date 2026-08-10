//package com.newzkl.platform.plugin.gateway;
//
//import cn.dev33.satoken.same.SaSameUtil;
//import cn.dev33.satoken.stp.StpUtil;
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
///**
// * 全局过滤器，为请求添加 Same-Token, 传递到子服务
// * @author fang
// */
//@Component
//public class ForwardAuthFilter implements GlobalFilter {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String ip = IpUtils.getRealIpAddress(exchange.getRequest());
//        ServerHttpRequest newRequest = exchange
//                .getRequest()
//                .mutate()
//                // 为请求追加 Same-Token 参数
//                .header(SaSameUtil.SAME_TOKEN, StpUtil.getTokenValue())
//                .header("X-Real-IP", new String[]{ip})
//                .build();
//        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
//        return chain.filter(newExchange);
//    }
//}
