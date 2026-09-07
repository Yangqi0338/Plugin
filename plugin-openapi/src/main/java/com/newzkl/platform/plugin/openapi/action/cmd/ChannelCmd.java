package com.newzkl.platform.plugin.openapi.action.cmd;

import lombok.Data;

/**
 * 开放平台-渠道商入参
 *
 * @author KC
 */
public class ChannelCmd {

    /**
     * 渠道商基础信息同步入参
     */
    @Data
    public static class ChannelBaseSyncReq {

        /**
         * 营业执照 对外契约保留 我方不落库
         */
        private String license;

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
}
