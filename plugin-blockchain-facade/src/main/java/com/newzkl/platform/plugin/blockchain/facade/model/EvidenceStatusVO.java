package com.newzkl.platform.plugin.blockchain.facade.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 存证状态视图
 *
 * <p>按存证编号查询返回, 供核验/展示</p>
 *
 * @author KC
 */
@Data
public class EvidenceStatusVO implements Serializable {

    /**
     * 存证编号
     */
    private String evidenceNo;

    /**
     * 存证业务类型
     */
    private String bizType;

    /**
     * 存证业务编号
     */
    private String bizNo;

    /**
     * 内容摘要 hash
     */
    private String contentHash;

    /**
     * 链上交易 hash (伪存证阶段为 null)
     */
    private String chainTxHash;

    /**
     * 存证摘要
     */
    private String summary;

    /**
     * 存证时间
     */
    private LocalDateTime evidenceTime;
}
