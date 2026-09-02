package com.newzkl.platform.plugin.audit.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.plugin.audit.infrastructure.entity.SpuWorkTableDO;
import com.newzkl.platform.plugin.audit.model.query.SpuWorkTableQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * SPU 工单审批数据 DAO
 *
 * @author KC
 */
@Mapper
@Repository
public interface SpuWorkTableDAO extends BaseMapper<SpuWorkTableDO> {

    /**
     * 构建工单审批数据查询条件
     *
     * @param query 查询条件
     * @return lambda 查询包装器
     */
    default BaseLambdaQueryWrapper<SpuWorkTableDO> getLw(SpuWorkTableQuery query) {
        return new BaseLambdaQueryWrapper<SpuWorkTableDO>()
                .notEmptyEq(SpuWorkTableDO::getAccountId, query.getAccountId())
                .notEmptyEq(SpuWorkTableDO::getSpuId, query.getSpuId())
                .notEmptyLike(SpuWorkTableDO::getSpuName, query.getSpuName())
                .notEmptyEq(SpuWorkTableDO::getOperateTarget, query.getOperateTarget())
                .notEmptyEq(SpuWorkTableDO::getOperateType, query.getOperateType())
                .notEmptyIn(SpuWorkTableDO::getAuditState, query.getAuditStateList())
                ;
    }
}
