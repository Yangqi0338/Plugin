package com.newzkl.platform.plugin.blockchain.infrastructure.client;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * MySQL 伪存证客户端 (MVP 默认)
 *
 * <p>不实际上链, hash + 编号落库即视为存证成功。用于演示/招商快速可用。
 * 真链就绪后由 ThirdPartyChainClient @Primary 覆盖本实现</p>
 *
 * @author KC
 */
@Primary
@Component
public class MysqlEvidenceClient implements EvidenceClient {

    /**
     * 客户端标识
     */
    private static final String CLIENT_ID = "mysql";

    @Override
    public String clientId() {
        return CLIENT_ID;
    }

    @Override
    public String submit(String contentHash) {
        // 伪存证: 无链上提交, 链上交易 hash 为 null
        return null;
    }
}
