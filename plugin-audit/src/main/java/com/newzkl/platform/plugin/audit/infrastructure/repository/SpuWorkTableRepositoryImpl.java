package com.newzkl.platform.plugin.audit.infrastructure.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.plugin.audit.infrastructure.dao.SpuWorkTableDAO;
import com.newzkl.platform.plugin.audit.infrastructure.entity.SpuWorkTableDO;
import com.newzkl.platform.plugin.audit.model.dto.SpuWorkTableDTO;
import com.newzkl.platform.plugin.audit.model.query.SpuWorkTableQuery;
import com.newzkl.platform.plugin.audit.domain.adapt.repository.SpuWorkTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * SPU 工单审批数据仓储实现
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class SpuWorkTableRepositoryImpl implements SpuWorkTableRepository {

    private final SpuWorkTableDAO spuWorkTableDAO;

    @Override
    public Long workTableSave(SpuWorkTableDTO workTable) {
        SpuWorkTableDO workTableDO = TransferUtils.transfer(workTable, SpuWorkTableDO::new);
        spuWorkTableDAO.insert(workTableDO);
        return workTableDO.getId();
    }

    @Override
    public int workTableEdit(SpuWorkTableDTO workTable) {
        return spuWorkTableDAO.updateById(TransferUtils.transfer(workTable, SpuWorkTableDO::new));
    }

    @Override
    public Page<SpuWorkTableDTO> workTablePage(SpuWorkTableQuery query) {
        Page<SpuWorkTableDO> page = spuWorkTableDAO.selectPage(RepositorySupport.page(query), spuWorkTableDAO.getLw(query));
        return TransferUtils.transferPage(page, SpuWorkTableDTO::new);
    }

    @Override
    public SpuWorkTableDTO workTable(Long workTableId) {
        return TransferUtils.transfer(spuWorkTableDAO.selectById(workTableId), SpuWorkTableDTO::new);
    }
}
