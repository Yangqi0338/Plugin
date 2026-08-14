package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品中心 - 分类统计
 */
@Data
public class ProductCategoryVO implements Serializable {

    /** 分类名称列表 */
    private List<String> categoryList;

    /** 商品数列表(与 categoryList 对齐) */
    private List<Integer> countList;
}