package com.newzkl.platform.plugin.bi.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.plugin.bi.infrastructure.entity.FactOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单事件事实表 DAO
 */
@Mapper
public interface FactOrderDAO extends BaseMapper<FactOrder> {
}
