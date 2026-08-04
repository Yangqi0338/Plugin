package com.newzkl.platform.plugin.openapi.model.query;

import com.zkl.scm.model.web.PageQuery;
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
}
