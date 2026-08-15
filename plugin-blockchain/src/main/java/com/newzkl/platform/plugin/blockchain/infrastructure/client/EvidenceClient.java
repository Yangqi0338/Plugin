package com.newzkl.platform.plugin.blockchain.infrastructure.client;

/**
 * 存证底层客户端 SPI (内部)
 *
 * <p>抽象"把内容 hash 提交到存证介质"这一步。MVP 走 {@link MysqlEvidenceClient} 伪存证,
 * 真链阶段新增 ThirdPartyChainClient 并 @Primary 覆盖, 上层 domain 零改动</p>
 *
 * @author KC
 */
public interface EvidenceClient {

    /**
     * 客户端标识 (落 block_evidence.client_id, 供追溯存证介质)
     *
     * @return 客户端标识
     */
    String clientId();

    /**
     * 提交内容 hash 到存证介质
     *
     * <p>伪存证阶段无实际提交(返回 null); 真链阶段返回链上交易 hash</p>
     *
     * @param contentHash 内容摘要 hash
     * @return 链上交易 hash (伪存证阶段为 null)
     */
    String submit(String contentHash);
}
