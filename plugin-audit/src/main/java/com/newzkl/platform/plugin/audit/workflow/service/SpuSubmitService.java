package com.newzkl.platform.plugin.audit.workflow.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.plugin.audit.port.AdminAccountPort;
import com.newzkl.platform.plugin.audit.port.GoodsSeatPort;
import com.newzkl.platform.plugin.audit.port.SpuReadPort;
import com.newzkl.platform.plugin.audit.port.SpuSubmitPort;
import com.newzkl.platform.plugin.audit.workflow.constant.AuditEnum;
import com.newzkl.platform.plugin.audit.workflow.model.AuditAccountView;
import com.newzkl.platform.plugin.audit.workflow.strategy.SpuWorkflowStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SPU提交审核服务
 *
 * <p>编排供应商提交SPU进入审核流程的4步骨架:
 * 1. 读spuVO获取supplierId
 * 2. 扣减供应商1个商品位
 * 3. 创建SPU_CREATE审批流
 * 4. 回写spu.spuSubmit状态</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpuSubmitService {

    private final SpuReadPort spuReadPort;
    private final GoodsSeatPort goodsSeatPort;
    private final SpuWorkflowStrategy spuWorkflowStrategy;
    private final SpuSubmitPort spuSubmitPort;
    private final AdminAccountPort adminAccountPort;

    /**
     * 供应商提交SPU进入审核
     *
     * @param spuId SPU主键
     * @param templateId 审批模板主键 (SPU_CREATE模板)
     * @return 审批流主键
     */
    @Transactional(rollbackFor = Exception.class)
    public Long submit(Long spuId, Long templateId) {
        // 步1: 读spuVO获取supplierId
        String spuInfoJson = spuReadPort.spuInfoJson(spuId);
        JSONObject spuVO = JSON.parseObject(spuInfoJson);
        Long supplierId = spuVO.getLong("supplierId");

        // 步2: 扣减供应商1个商品位
        goodsSeatPort.supplierSubmitSubGoodsSeat(supplierId, spuId);

        // 步3: 创建SPU_CREATE审批流
        AuditAccountView account = adminAccountPort.currentAccount();
        Long flowId = spuWorkflowStrategy.apply(templateId, account, spuInfoJson);

        // 步4: 回写spu.spuSubmit状态 (spuId + flowId)
        spuSubmitPort.spuSubmit(spuId, flowId);

        return flowId;
    }
}
