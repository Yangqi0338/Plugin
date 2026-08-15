package com.newzkl.platform.plugin.blockchain.infrastructure.facade;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.newzkl.platform.plugin.blockchain.facade.EvidenceFacade;
import com.newzkl.platform.plugin.blockchain.facade.model.EvidenceRequest;
import com.newzkl.platform.plugin.blockchain.facade.model.EvidenceResult;
import com.newzkl.platform.plugin.blockchain.facade.model.EvidenceStatusVO;
import com.newzkl.platform.plugin.blockchain.infrastructure.client.EvidenceClient;
import com.newzkl.platform.plugin.blockchain.infrastructure.dao.BlockEvidenceDAO;
import com.newzkl.platform.plugin.blockchain.infrastructure.entity.BlockEvidenceDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 知链存证对外契约实现 (provider)
 *
 * <p>对内容算 SHA-256 摘要, 经 {@link EvidenceClient} 提交存证介质(伪存证/真链),
 * 生成存证编号 {@code CKC-{yyyyMMdd}-{序号}} 后落库</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EvidenceFacadeImpl implements EvidenceFacade {

    /**
     * 存证编号前缀
     */
    private static final String EVIDENCE_NO_PREFIX = "CKC";

    private final EvidenceClient evidenceClient;
    private final BlockEvidenceDAO blockEvidenceDAO;

    @Override
    public EvidenceResult evidence(EvidenceRequest request) {
        String contentHash = SecureUtil.sha256(StrUtil.nullToEmpty(request.getContent()));
        String chainTxHash = evidenceClient.submit(contentHash);
        LocalDateTime now = LocalDateTime.now();
        String evidenceNo = generateEvidenceNo(now);

        BlockEvidenceDO entity = new BlockEvidenceDO();
        entity.setClientId(evidenceClient.clientId());
        entity.setBizType(request.getBizType());
        entity.setBizNo(request.getBizNo());
        entity.setTenantId(request.getClientId());
        entity.setUserId(request.getUserId());
        entity.setContentHash(contentHash);
        entity.setSummary(request.getSummary());
        entity.setEvidenceNo(evidenceNo);
        entity.setChainTxHash(chainTxHash);
        entity.setEvidenceTime(now);
        blockEvidenceDAO.insert(entity);

        log.info("存证成功, bizType: {}, bizNo: {}, evidenceNo: {}", request.getBizType(), request.getBizNo(), evidenceNo);
        EvidenceResult result = new EvidenceResult();
        result.setEvidenceNo(evidenceNo);
        result.setContentHash(contentHash);
        result.setChainTxHash(chainTxHash);
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    @Override
    public EvidenceStatusVO query(String evidenceNo) {
        BlockEvidenceDO entity = findByEvidenceNo(evidenceNo);
        if (entity == null) {
            return null;
        }
        EvidenceStatusVO vo = new EvidenceStatusVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    @Override
    public boolean verifyHash(String evidenceNo, String contentHash) {
        BlockEvidenceDO entity = findByEvidenceNo(evidenceNo);
        return entity != null && StrUtil.equals(entity.getContentHash(), contentHash);
    }

    /**
     * 按存证编号查存证记录
     *
     * @param evidenceNo 存证编号
     * @return 存证记录 (不存在返回 null)
     */
    private BlockEvidenceDO findByEvidenceNo(String evidenceNo) {
        return blockEvidenceDAO.selectOne(new LambdaQueryWrapper<BlockEvidenceDO>()
                .eq(BlockEvidenceDO::getEvidenceNo, evidenceNo)
                .last("limit 1"));
    }

    /**
     * 生成存证编号 CKC-{yyyyMMdd}-{当日序号}
     *
     * <p>MVP 用当日已有存证数 +1 作序号(6 位补零)。高并发下序号可能冲突,
     * 后续可换分布式序列(见 deferred)</p>
     *
     * @param now 存证时间
     * @return 存证编号
     */
    private String generateEvidenceNo(LocalDateTime now) {
        String dateStr = DateUtil.format(now, "yyyyMMdd");
        LocalDateTime dayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime dayEnd = now.toLocalDate().atTime(LocalTime.MAX);
        Long count = blockEvidenceDAO.selectCount(new LambdaQueryWrapper<BlockEvidenceDO>()
                .between(BlockEvidenceDO::getEvidenceTime, dayStart, dayEnd));
        long seq = (count == null ? 0L : count) + 1L;
        return StrUtil.format("{}-{}-{}", EVIDENCE_NO_PREFIX, dateStr, StrUtil.padPre(String.valueOf(seq), 6, '0'));
    }
}
