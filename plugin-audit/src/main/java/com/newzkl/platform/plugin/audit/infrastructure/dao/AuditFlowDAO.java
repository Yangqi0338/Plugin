package com.newzkl.platform.plugin.audit.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.plugin.audit.infrastructure.entity.AuditFlowDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 审批流 DAO
 *
 * @author KC
 */
@Mapper
@Repository
public interface AuditFlowDAO extends BaseMapper<AuditFlowDO> {
}
