package com.newzkl.platform.plugin.blockchain.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseIdDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 知链存证记录
 *
 * <p>一条业务数据的存证快照, 存内容摘要 hash + 存证编号。伪存证阶段 chainTxHash 为 null,
 * 真链阶段回填链上交易 hash</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("block_evidence")
public class BlockEvidenceDO extends BaseIdDO {

    /**
     * 存证客户端标识 (mysql/真链名)
     */
    private String clientId;

    /**
     * 存证业务类型 (ORDER/GOODS/USER_CONSUME)
     */
    private String bizType;

    /**
     * 存证业务编号 (订单号/商品ID/用户ID)
     */
    private String bizNo;

    /**
     * 关联租户ID
     */
    private Long tenantId;

    /**
     * 关联用户ID
     */
    private Long userId;

    /**
     * 内容摘要 hash (SHA-256 十六进制)
     */
    private String contentHash;

    /**
     * 存证摘要 (人可读)
     */
    private String summary;

    /**
     * 存证编号 (CKC-{yyyyMMdd}-{序号})
     */
    private String evidenceNo;

    /**
     * 链上交易 hash (伪存证阶段为 null)
     */
    private String chainTxHash;

    /**
     * 存证时间
     */
    private LocalDateTime evidenceTime;
}
