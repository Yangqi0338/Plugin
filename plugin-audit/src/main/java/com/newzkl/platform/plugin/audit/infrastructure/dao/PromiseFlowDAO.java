package com.newzkl.platform.plugin.audit.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.plugin.audit.infrastructure.entity.PromiseFlowDO;
import com.newzkl.platform.plugin.audit.workflow.model.PromiseFlowQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 保证金流水 DAO
 *
 * @author KC
 */
@Mapper
@Repository
public interface PromiseFlowDAO extends BaseMapper<PromiseFlowDO> {

    /**
     * 构建保证金流水查询条件
     *
     * @param query 查询条件
     * @return lambda 查询包装器
     */
    default BaseLambdaQueryWrapper<PromiseFlowDO> getLw(PromiseFlowQuery query) {
        return new BaseLambdaQueryWrapper<PromiseFlowDO>()
                .notEmptyEq(PromiseFlowDO::getAccountId, query.getAccountId())
                .notEmptyEq(PromiseFlowDO::getAuditState, query.getAuditState())
                .notEmptyIn(PromiseFlowDO::getAuditState, query.getAuditStateList())
                ;
    }
}
