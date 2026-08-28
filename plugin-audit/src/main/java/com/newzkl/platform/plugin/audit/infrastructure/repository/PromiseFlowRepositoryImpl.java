package com.newzkl.platform.plugin.audit.infrastructure.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.plugin.audit.infrastructure.dao.PromiseFlowDAO;
import com.newzkl.platform.plugin.audit.infrastructure.entity.PromiseFlowDO;
import com.newzkl.platform.plugin.audit.workflow.model.PromiseFlow;
import com.newzkl.platform.plugin.audit.workflow.model.PromiseFlowQuery;
import com.newzkl.platform.plugin.audit.workflow.repository.PromiseFlowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 保证金流水仓储实现
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class PromiseFlowRepositoryImpl implements PromiseFlowRepository {

    private final PromiseFlowDAO promiseFlowDAO;

    @Override
    public Long promiseFlowSave(PromiseFlow promiseFlow) {
        PromiseFlowDO promiseFlowDO = TransferUtils.transfer(promiseFlow, PromiseFlowDO::new);
        promiseFlowDAO.insert(promiseFlowDO);
        return promiseFlowDO.getId();
    }

    @Override
    public int promiseFlowEdit(PromiseFlow promiseFlow) {
        return promiseFlowDAO.updateById(TransferUtils.transfer(promiseFlow, PromiseFlowDO::new));
    }

    @Override
    public Page<PromiseFlow> promiseFlowPage(PromiseFlowQuery query) {
        Page<PromiseFlowDO> page = promiseFlowDAO.selectPage(RepositorySupport.page(query), promiseFlowDAO.getLw(query));
        return TransferUtils.transferPage(page, PromiseFlow::new);
    }

    @Override
    public PromiseFlow promiseFlow(Long promiseFlowId) {
        return TransferUtils.transfer(promiseFlowDAO.selectById(promiseFlowId), PromiseFlow::new);
    }
}
