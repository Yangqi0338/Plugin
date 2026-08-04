package com.newzkl.platform.plugin.openapi.action.cmd;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/159:43
 */
public class RefundCmd {
    @Data
    public static class RefundId {
        /**
         * 售后单号
         */
        @NotNull
        private Long refundId;
    }
    @Data
    public static class RefundIdList {
        /**
         * 售后单号集合
         */
        @NotEmpty
        private List<Long> refundIdList;
    }
}
