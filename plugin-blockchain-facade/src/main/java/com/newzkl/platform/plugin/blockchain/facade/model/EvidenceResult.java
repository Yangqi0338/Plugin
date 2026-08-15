package com.newzkl.platform.plugin.blockchain.facade.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 存证结果
 *
 * <p>存证成功后返回, 核心是存证编号 + 内容 hash。真链阶段附带链上 txHash</p>
 *
 * @author KC
 */
@Data
public class EvidenceResult implements Serializable {

    /**
     * 存证编号 (格式 CKC-{yyyyMMdd}-{序号})
     */
    private String evidenceNo;

    /**
     * 内容摘要 hash (SHA-256 十六进制)
     */
    private String contentHash;

    /**
     * 链上交易 hash (伪存证阶段为 null, 真链阶段回填)
     */
    private String chainTxHash;

    /**
     * 是否成功
     */
    private Boolean success;
}
