package com.newzkl.platform.plugin.openapi.model.command;

import lombok.Data;

/**
 * 渠道商基础信息同步命令
 *
 * <p>由 controller 从 {@code ChannelCmd.ChannelBaseSyncReq} 一行 TransferUtils 转成
 * 刻意不含 license: 对外契约收该字段但我方不落库(ChannelDO 无此列), 目标类没这个字段
 * 后人便无从误 set</p>
 *
 * @author KC
 */
@Data
public class ChannelSyncCommand {

    /**
     * 发货省编码
     */
    private Integer shipProvinceCode;

    /**
     * 发货市编码
     */
    private Integer shipCityCode;

    /**
     * 发货区编码
     */
    private Integer shipAreaCode;

    /**
     * 联系人姓名
     */
    private String contactsName;

    /**
     * 店铺名称
     */
    private String storeName;
}
