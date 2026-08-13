package com.newzkl.platform.plugin.bi.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.plugin.bi.infrastructure.entity.DwsRealtimeOrderTrend;
import org.apache.ibatis.annotations.Mapper;

/**
 * 首页实时宽表 DAO(事件行, 仅当日)
 */
@Mapper
public interface DwsRealtimeOrderTrendDAO extends BaseMapper<DwsRealtimeOrderTrend> {
}
