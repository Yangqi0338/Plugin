package com.newzkl.platform.plugin.bi.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.plugin.bi.infrastructure.entity.FactBlockchain;
import org.apache.ibatis.annotations.Mapper;

/**
 * 知链存证事件事实表 DAO
 */
@Mapper
public interface FactBlockchainDAO extends BaseMapper<FactBlockchain> {
}
