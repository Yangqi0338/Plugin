package com.newzkl.platform.plugin.blockchain.model.query;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 存证记录查询
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EvidenceQuery extends PageQuery {

    /**
     * 存证业务类型
     */
    private String bizType;

    /**
     * 存证业务编号
     */
    private String bizNo;

    /**
     * 存证编号
     */
    private String evidenceNo;

    /**
     * 关联用户ID
     */
    private Long userId;
}
