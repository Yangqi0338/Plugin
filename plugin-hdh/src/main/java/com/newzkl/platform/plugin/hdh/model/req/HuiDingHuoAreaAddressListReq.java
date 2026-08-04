package com.newzkl.platform.plugin.hdh.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取省份地区列表请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuiDingHuoAreaAddressListReq extends HuiDingHuoBaseReq {
    // 仅需appId，无额外参数
}