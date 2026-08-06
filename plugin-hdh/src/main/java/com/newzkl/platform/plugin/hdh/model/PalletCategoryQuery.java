package com.newzkl.platform.plugin.hdh.model;

import lombok.Data;

/**
 * 货盘分类查询条件
 *
 * <p>迁自 new-scm scm-goods {@code PalletCategoryQuery}, 承接 palletCategoryList 入参。
 * 会订货分类为全量返回, 入参当前仅占位保留源契约</p>
 *
 * @author KC
 */
@Data
public class PalletCategoryQuery {

    /** 是否不分页 */
    private Boolean nonPaged;

    /** 父 ID */
    private Long pid;

    /** 分类名称 */
    private String name;
}
