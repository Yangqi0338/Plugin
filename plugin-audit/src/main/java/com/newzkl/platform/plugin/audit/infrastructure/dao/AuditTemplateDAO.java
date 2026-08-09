package com.newzkl.platform.plugin.audit.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.plugin.audit.infrastructure.entity.AuditTemplateDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 审批模板 DAO
 *
 * @author KC
 */
@Mapper
@Repository
public interface AuditTemplateDAO extends BaseMapper<AuditTemplateDO> {
}
