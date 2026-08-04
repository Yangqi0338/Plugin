package com.newzkl.platform.plugin.audit.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.plugin.audit.infrastructure.dao.po.AuditPluginTemplateDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 审批模板 DAO
 *
 * @author KC
 */
@Mapper
@Repository
public interface AuditPluginTemplateDAO extends BaseMapper<AuditPluginTemplateDO> {
}
