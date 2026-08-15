package com.newzkl.platform.plugin.blockchain.facade;

import com.newzkl.platform.plugin.blockchain.facade.model.EvidenceRequest;
import com.newzkl.platform.plugin.blockchain.facade.model.EvidenceResult;
import com.newzkl.platform.plugin.blockchain.facade.model.EvidenceStatusVO;

/**
 * 知链存证对外契约 (provider facade)
 *
 * <p>业务侧只依赖本接口完成存证/核验, 不感知链底层实现。
 * 由 plugin-blockchain 提供实现, MVP 阶段走伪存证(MySQL), 真链阶段仅换底层 client 实现</p>
 *
 * @author KC
 */
public interface EvidenceFacade {

    /**
     * 存证
     *
     * <p>对请求内容算摘要 hash, 生成存证编号后落库(或上链), 返回存证结果</p>
     *
     * @param request 存证请求
     * @return 存证结果 (含存证编号 + 内容 hash)
     */
    EvidenceResult evidence(EvidenceRequest request);

    /**
     * 按存证编号查询存证状态
     *
     * @param evidenceNo 存证编号
     * @return 存证状态视图 (不存在返回 null)
     */
    EvidenceStatusVO query(String evidenceNo);

    /**
     * 校验内容 hash 与存证记录是否一致
     *
     * @param evidenceNo  存证编号
     * @param contentHash 待校验内容 hash
     * @return 一致返回 true
     */
    boolean verifyHash(String evidenceNo, String contentHash);
}
