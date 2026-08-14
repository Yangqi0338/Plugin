package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.util.List;

/**
 * 数据洞察·品类销售占比
 */
@Data
public class InsightCategoryVO {

    private List<CategoryItem> categoryList;

    @Data
    public static class CategoryItem {
        /** 品类名 */
        private String name;
        /** 占比(%) */
        private Integer ratio;
    }
}
