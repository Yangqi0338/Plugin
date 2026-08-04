package com.newzkl.platform.plugin.audit.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.plugin.audit.infrastructure.dao.po.AuditPluginFlowDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 审批流 DAO
 *
 * @author KC
 */
@Mapper
@Repository
public interface AuditPluginFlowDAO extends BaseMapper<AuditPluginFlowDO> {
}
