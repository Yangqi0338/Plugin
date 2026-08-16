package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品中心分类统计
 */
@Data
public class ProductCategoryRes implements Serializable {

    /** 分类列表 */
    private List<CategoryItem> categoryList;

    /**
     * 分类项
     */
    @Data
    public static class CategoryItem {

        /** 分类ID */
        private Long categoryId;

        /** 商品数量 */
        private Integer count;
    }
}
