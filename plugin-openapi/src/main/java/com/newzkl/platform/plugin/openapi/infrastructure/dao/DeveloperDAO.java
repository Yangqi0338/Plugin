package com.newzkl.platform.plugin.openapi.infrastructure.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.plugin.openapi.model.query.DeveloperQuery;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperVO;
import com.newzkl.platform.plugin.openapi.infrastructure.entity.DeveloperDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 开发者 DAO
 *
 * @author fang
 */
@Mapper
public interface DeveloperDAO {

    /**
     * 插入开发者
     *
     * @param model 开发者DO
     * @return 影响行数
     */
    int insert(@Param("model") DeveloperDO model);

    /**
     * 主键查询
     *
     * @param id 主键
     * @return 开发者DO
     */
    DeveloperDO selectByPrimaryKey(@Param("id") Long id);

    /**
     * 条件分页查询
     *
     * @param page  分页参数
     * @param query 查询条件
     * @return 开发者VO分页
     */
    Page<DeveloperVO> listByQuery(Page<?> page, @Param("query") DeveloperQuery query);

    /**
     * 按appId查询
     *
     * @param appId 开发者appId
     * @return 开发者VO
     */
    DeveloperVO developerVOByAppId(@Param("appId") String appId);

    /**
     * 按账号ID查询
     *
     * @param accountId 账号ID
     * @return 开发者VO
     */
    DeveloperVO developerVOByAccountId(@Param("accountId") Long accountId);
}