package com.newzkl.platform.plugin.openapi.infrastructure.assembler;

import com.newzkl.platform.plugin.openapi.model.entity.Developer;
import com.newzkl.platform.plugin.openapi.model.vo.DeveloperVO;
import com.newzkl.platform.plugin.openapi.infrastructure.assembler.DeveloperConvert;
import com.newzkl.platform.plugin.openapi.infrastructure.entity.DeveloperDO;
import org.mapstruct.Mapper;

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
     * DO转VO
     * @param developerDO
     * @return
     */
    DeveloperVO doToVO(DeveloperDO developerDO);
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
    DeveloperVO domainToVO(Developer developer);
}
