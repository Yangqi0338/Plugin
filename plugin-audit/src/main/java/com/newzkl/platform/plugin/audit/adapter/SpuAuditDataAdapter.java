package com.newzkl.platform.plugin.audit.adapter;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.AuditDataSpuRepository;
import com.newzkl.platform.base.biz.goods.model.goods.entity.audit.AuditDataSpu;
import com.newzkl.platform.base.biz.goods.model.goods.query.audit.AuditDataSpuQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataSpuVO;
import com.newzkl.platform.plugin.audit.port.SpuAuditDataPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * SPU 上传审批数据适配器
 *
 * <p>转调 biz-goods AuditDataSpuRepository 读写 SPU 上传审批数据, 边界处做 JSON 与 Base VO/Query 互转</p>
 *
 * @author KC
 */
@Component("auditPluginSpuAuditDataAdapter")
@RequiredArgsConstructor
public class SpuAuditDataAdapter implements SpuAuditDataPort {

    private final AuditDataSpuRepository auditDataSpuRepository;

    @Override
    public String detail(Long flowId) {
        AuditDataSpuVO vo = auditDataSpuRepository.auditDataSpuVO(flowId);
        return vo == null ? null : JSON.toJSONString(vo);
    }

    @Override
    public String pageJson(String queryJson) {
        AuditDataSpuQuery query = JSON.parseObject(queryJson, AuditDataSpuQuery.class);
        Page<AuditDataSpuVO> page = auditDataSpuRepository.auditDataSpuPageVOList(query);
        return JSON.toJSONString(page);
    }

    @Override
    public Long save(String dataVoJson) {
        AuditDataSpuVO vo = JSON.parseObject(dataVoJson, AuditDataSpuVO.class);
        AuditDataSpu entity = auditDataSpuRepository.voToAuditDataSpu(vo);
        return auditDataSpuRepository.auditDataSpuSave(entity);
    }
}
