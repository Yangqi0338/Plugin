package com.newzkl.platform.plugin.bi.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.plugin.bi.infrastructure.entity.FactGoods;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品事件事实表 DAO
 */
@Mapper
public interface FactGoodsDAO extends BaseMapper<FactGoods> {
}
