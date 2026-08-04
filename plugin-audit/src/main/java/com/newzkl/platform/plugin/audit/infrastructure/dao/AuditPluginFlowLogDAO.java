package com.newzkl.platform.plugin.audit.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.plugin.audit.infrastructure.dao.po.AuditPluginFlowLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 审批日志 DAO
 *
 * @author KC
 */
@Mapper
@Repository
public interface AuditPluginFlowLogDAO extends BaseMapper<AuditPluginFlowLogDO> {
}
