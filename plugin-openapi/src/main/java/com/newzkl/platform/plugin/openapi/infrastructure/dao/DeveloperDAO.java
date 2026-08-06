package com.newzkl.platform.plugin.openapi.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.plugin.openapi.infrastructure.entity.DeveloperDO;
import com.newzkl.platform.plugin.openapi.model.query.DeveloperQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * 开发者 DAO
 *
 * @author fang
 */
@Mapper
public interface DeveloperDAO extends BaseMapper<DeveloperDO> {

    /**
     * 构建开发者查询条件
     *
     * @param query 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<DeveloperDO> getLw(DeveloperQuery query) {
        BaseLambdaQueryWrapper<DeveloperDO> wrapper = new BaseLambdaQueryWrapper<DeveloperDO>()
                .notNullEq(DeveloperDO::getId, query.getId())
                .notEmptyIn(DeveloperDO::getId, query.getIdList())
                .notEmptyEq(DeveloperDO::getAppId, query.getAppId())
                .notNullEq(DeveloperDO::getAccountId, query.getAccountId());
        wrapper.orderByDesc(DeveloperDO::getCreateTime);
        return wrapper;
    }
}
