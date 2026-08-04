package com.newzkl.platform.plugin.openapi.action.cmd;

import lombok.Data;
import java.util.List;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/159:43
 */
public class OrderCmd {
    @Data
    public static class OrderId {
        /**
         * 外部订单号
         */
        private String outOrderNo;
    }
    @Data
    public static class OrderIdList {
        /**
         * 外部订单号集合
         */
        private List<String> outOrderNoList;
    }
}
