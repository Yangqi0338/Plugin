package com.newzkl.platform.plugin.openapi.model.query;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import java.util.List;

/**
* 开发者
* @author fang
*/
@Data
public class DeveloperQuery extends PageQuery {

    /**
     * ID
     */
    private Long id;
    /**
     * ID集合
     */
    private List<Long> idList;
    /**
     * 开发者appId
     */
    private String appId;
    /**
     * 账号ID
     */
    private Long accountId;
}
