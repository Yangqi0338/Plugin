package com.newzkl.platform.plugin.openapi.infrastructure.dao;

import com.zkl.scm.mybatis.model.MyBatisPageDao;
import com.zkl.scm.openapi.domain.developer.model.query.DeveloperQuery;
import com.zkl.scm.openapi.domain.developer.model.vo.DeveloperVO;
import com.zkl.scm.openapi.infrastructure.entity.DeveloperDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 用户账号
* @author fang
*/
@Mapper
public interface DeveloperDAO extends MyBatisPageDao<DeveloperVO, DeveloperQuery, DeveloperDO, Long> {

    DeveloperVO developerVOByAppId(@Param("appId") String appId);

    DeveloperVO developerVOByAccountId(@Param("accountId") Long accountId);
}