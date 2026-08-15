package com.newzkl.platform.plugin.blockchain.facade.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 存证请求
 *
 * <p>业务侧构造并传入知链存证入口。业务不感知链底层, 仅提供业务标识 + 待存证内容摘要</p>
 *
 * @author KC
 */
@Data
public class EvidenceRequest implements Serializable {

    /**
     * 存证业务类型 (ORDER/GOODS/USER_CONSUME 等)
     */
    private String bizType;

    /**
     * 存证业务编号 (订单号/商品ID/用户ID 等)
     */
    private String bizNo;

    /**
     * 租户ID
     */
    private Long clientId;

    /**
     * 关联用户ID
     */
    private Long userId;

    /**
     * 待存证内容 (JSON, 由业务序列化后传入, 知链对其算摘要 hash)
     */
    private String content;

    /**
     * 存证摘要 (人可读, 展示用)
     */
    private String summary;
}
