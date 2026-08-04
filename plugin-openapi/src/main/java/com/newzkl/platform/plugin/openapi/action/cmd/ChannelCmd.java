package com.newzkl.platform.plugin.openapi.action.cmd;

import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/159:43
 */
public class ChannelCmd {
    @Data
    public static class SpuIdReq {
        /**
         * spuId
         */
        private Long spuId;
    }
}
