package com.newzkl.platform.plugin.blockchain.infrastructure.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 第三方区块链存证客户端 (留桩, 真链切换用)
 *
 * <p>配置 {@code blockchain.chain.enabled=true} 时激活并 @Primary 覆盖 {@link MysqlEvidenceClient}。
 * 当前未接第三方链 SDK(缺链选型/凭证/依赖授权, 见 deferred), submit 暂抛未实现,
 * 提示接线; SDK 接入后在此实现真实上链并返回链上交易 hash</p>
 *
 * @author KC
 */
@Slf4j
@Primary
@Component
@ConditionalOnProperty(prefix = "blockchain.chain", name = "enabled", havingValue = "true")
public class ThirdPartyChainClient implements EvidenceClient {

    /**
     * 客户端标识
     */
    private static final String CLIENT_ID = "third-party-chain";

    @Override
    public String clientId() {
        return CLIENT_ID;
    }

    @Override
    public String submit(String contentHash) {
        // 待接线: 第三方链 SDK 未接入(缺链选型/凭证/依赖), 见 deferred-issues
        throw new UnsupportedOperationException("第三方链存证未接入: 需完成链选型 + SDK 依赖授权 + 凭证配置");
    }
}
