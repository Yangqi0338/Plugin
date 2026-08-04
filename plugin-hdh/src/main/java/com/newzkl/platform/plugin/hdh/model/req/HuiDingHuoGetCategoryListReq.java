package com.newzkl.platform.plugin.hdh.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取类目列表请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoGetCategoryListReq extends HuiDingHuoBaseReq {
    // 仅需继承基类的appId，无额外参数
}