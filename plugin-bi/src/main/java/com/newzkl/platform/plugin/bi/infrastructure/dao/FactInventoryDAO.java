package com.newzkl.platform.plugin.bi.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.plugin.bi.infrastructure.entity.FactInventory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存事件事实表 DAO
 */
@Mapper
public interface FactInventoryDAO extends BaseMapper<FactInventory> {
}
