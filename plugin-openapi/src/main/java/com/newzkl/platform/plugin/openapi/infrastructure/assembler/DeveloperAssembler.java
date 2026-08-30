package com.newzkl.platform.plugin.openapi.infrastructure.assembler;

import com.newzkl.platform.plugin.openapi.model.entity.Developer;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperRes;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperVO;
import com.newzkl.platform.plugin.openapi.infrastructure.assembler.DeveloperConvert;
import com.newzkl.platform.plugin.openapi.infrastructure.entity.DeveloperDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
* 用户账号
* @author fang
*/
@Mapper(componentModel = "spring", uses = DeveloperConvert.class)
public interface DeveloperAssembler {
    /**
     * DO转Domain
     * @param developerDO
     * @return
    */
    Developer doToDomain(DeveloperDO developerDO);
    /**
     * DO转VO(executor 嵌套平铺到 BaseRes 顶层)
     * @param developerDO
     * @return
     */
//    @Mapping(target = "creatorName", source = "executor.creatorName")
//    @Mapping(target = "updater", source = "executor.updater")
//    @Mapping(target = "updaterName", source = "executor.updaterName")
    DeveloperRes doToVO(DeveloperDO developerDO);
    /**
     * Domain转DO
     * @param developer
     * @return
     */
    DeveloperDO domainToDO(Developer developer);
    /**
     * Domain转VO
     * @param developer
     * @return
     */
    DeveloperRes domainToVO(Developer developer);
    /**
     * Res转对外VO(record)
     * @param developerRes
     * @return
     */
    DeveloperVO resToVO(DeveloperRes developerRes);
}
