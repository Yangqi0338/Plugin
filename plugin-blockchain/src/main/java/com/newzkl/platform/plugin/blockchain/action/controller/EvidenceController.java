package com.newzkl.platform.plugin.blockchain.action.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.blockchain.facade.EvidenceFacade;
import com.newzkl.platform.plugin.blockchain.facade.model.EvidenceRequest;
import com.newzkl.platform.plugin.blockchain.facade.model.EvidenceResult;
import com.newzkl.platform.plugin.blockchain.facade.model.EvidenceStatusVO;
import com.newzkl.platform.plugin.blockchain.infrastructure.dao.BlockEvidenceDAO;
import com.newzkl.platform.plugin.blockchain.infrastructure.entity.BlockEvidenceDO;
import com.newzkl.platform.plugin.blockchain.model.query.EvidenceQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.util.StrUtil;

/**
 * 知链存证控制器
 *
 * <p>存证 + 基础查询, 走大接口模式。证书/核验流水后续阶段补</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/blockchain/evidence")
@RequiredArgsConstructor
public class EvidenceController {

    private final EvidenceFacade evidenceFacade;
    private final BlockEvidenceDAO blockEvidenceDAO;

    /**
     * 存证
     *
     * @param request 存证请求
     * @return 存证结果 (含存证编号)
     */
    @PostMapping("/save")
    public PlatformResult<EvidenceResult> save(@RequestBody EvidenceRequest request) {
        return PlatformResult.success(evidenceFacade.evidence(request));
    }

    /**
     * 按存证编号查询存证状态
     *
     * @param query 查询条件 (取 evidenceNo)
     * @return 存证状态视图
     */
    @PostMapping("/detail")
    public PlatformResult<EvidenceStatusVO> detail(@RequestBody EvidenceQuery query) {
        return PlatformResult.success(evidenceFacade.query(query.getEvidenceNo()));
    }

    /**
     * 存证记录分页查询
     *
     * @param query 查询条件
     * @return 存证记录分页
     */
    @PostMapping("/jsonPageQuery")
    public PlatformResult<IPage<BlockEvidenceDO>> jsonPageQuery(@RequestBody EvidenceQuery query) {
        LambdaQueryWrapper<BlockEvidenceDO> wrapper = new LambdaQueryWrapper<BlockEvidenceDO>()
                .eq(StrUtil.isNotBlank(query.getBizType()), BlockEvidenceDO::getBizType, query.getBizType())
                .eq(StrUtil.isNotBlank(query.getBizNo()), BlockEvidenceDO::getBizNo, query.getBizNo())
                .eq(StrUtil.isNotBlank(query.getEvidenceNo()), BlockEvidenceDO::getEvidenceNo, query.getEvidenceNo())
                .eq(query.getUserId() != null, BlockEvidenceDO::getUserId, query.getUserId())
                .orderByDesc(BlockEvidenceDO::getEvidenceTime);
        Page<BlockEvidenceDO> page = new Page<>(query.getPageNo(), query.getPageSize());
        return PlatformResult.success(blockEvidenceDAO.selectPage(page, wrapper));
    }
}
