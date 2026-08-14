package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 首页工作台 - 营销渠道收入比例
 *
 * <p>对应用户首页「营销渠道收入比例」饼图: 各渠道名称 + 占比。</p>
 */
@Data
public class HomeChannelRevenueVO implements Serializable {

    /** 渠道名称列表(与 ratioList 对齐) */
    private List<String> channelList;

    /** 占比列表(百分比整数, 如 38, 16, 12... 与 channelList 对齐) */
    private List<Integer> ratioList;
}