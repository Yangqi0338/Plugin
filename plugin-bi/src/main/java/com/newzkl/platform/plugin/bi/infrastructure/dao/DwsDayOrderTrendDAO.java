package com.newzkl.platform.plugin.bi.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.plugin.bi.infrastructure.entity.DwsDayOrderTrend;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单趋势日宽表 DAO(T+1)
 */
@Mapper
public interface DwsDayOrderTrendDAO extends BaseMapper<DwsDayOrderTrend> {
}
