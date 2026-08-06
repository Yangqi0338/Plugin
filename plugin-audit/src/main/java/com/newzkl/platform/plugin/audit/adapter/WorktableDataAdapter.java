package com.newzkl.platform.plugin.audit.adapter;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.AuditDataWorkTableRepository;
import com.newzkl.platform.base.biz.goods.model.goods.entity.audit.AuditDataWorkTable;
import com.newzkl.platform.base.biz.goods.model.goods.query.audit.AuditDataWorkTableQuery;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataWorkTableVO;
import com.newzkl.platform.plugin.audit.port.WorktableDataPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工单审批数据适配器
 *
 * <p>转调 biz-goods AuditDataWorkTableRepository 读写工单审批数据, 边界处做 JSON 与 Base VO/Query 互转</p>
 *
 * <p>editSkuSalePrice 不转调 WorkTableWorkflowDomain.editBusinessData: 源实现建新实体只 setSkuSalePriceJson
 * 未 setId, 保存走 insert 而非 update (源缺陷)。本适配器按 id 取 VO 回填后保存, 走 update 路径修正之</p>
 *
 * @author KC
 */
@Component("auditPluginWorktableDataAdapter")
@RequiredArgsConstructor
public class WorktableDataAdapter implements WorktableDataPort {

    private final AuditDataWorkTableRepository auditDataWorkTableRepository;

    @Override
    public Long save(String dataVoJson) {
        AuditDataWorkTableVO vo = JSON.parseObject(dataVoJson, AuditDataWorkTableVO.class);
        AuditDataWorkTable entity = auditDataWorkTableRepository.voToAuditDataWorkTable(vo);
        return auditDataWorkTableRepository.auditDataWorkTableInsert(entity);
    }

    @Override
    public String detail(Long id) {
        AuditDataWorkTableVO vo = auditDataWorkTableRepository.auditDataWorkTableVO(id);
        return vo == null ? null : JSON.toJSONString(vo);
    }

    @Override
    public List<String> listByQuery(String queryJson) {
        AuditDataWorkTableQuery query = JSON.parseObject(queryJson, AuditDataWorkTableQuery.class);
        List<AuditDataWorkTableVO> voList = auditDataWorkTableRepository.auditDataWorkTableVOList(query);
        return voList.stream().map(JSON::toJSONString).collect(Collectors.toList());
    }

    @Override
    public String pageJson(String queryJson) {
        AuditDataWorkTableQuery query = JSON.parseObject(queryJson, AuditDataWorkTableQuery.class);
        Page<AuditDataWorkTableVO> page = auditDataWorkTableRepository.auditDataWorkTablePageVOList(query);
        return JSON.toJSONString(page);
    }

    @Override
    public void delete(List<Long> ids) {
        auditDataWorkTableRepository.auditDataWorkTableDelete(ids);
    }

    @Override
    public void editSkuSalePrice(Long id, String skuSalePriceJson) {
        AuditDataWorkTableVO vo = auditDataWorkTableRepository.auditDataWorkTableVO(id);
        if (vo == null) {
            return;
        }
        vo.setSkuSalePriceJson(skuSalePriceJson);
        AuditDataWorkTable entity = auditDataWorkTableRepository.voToAuditDataWorkTable(vo);
        auditDataWorkTableRepository.auditDataWorkTableEdit(entity);
    }
}
