package com.newzkl.platform.plugin.hdh.model;

import lombok.Data;

import java.util.List;

/**
 * 货盘 SPU 查询条件
 *
 * <p>迁自 new-scm scm-goods {@code PalletSpuQuery}, 承接 palletSpuPage/palletSpu 入参。
 * 仅支持会订货渠道 (spuChannelSource=2)</p>
 *
 * @author KC
 */
@Data
public class PalletSpuQuery {

    /** 页码 */
    private Integer pageNo = 1;

    /** 每页条数 */
    private Integer pageSize = 10;

    /** 渠道来源 (0平台 1怡亚通 2会订货) */
    private Integer spuChannelSource;

    /** 外部商品 ID (详情用) */
    private String outSpuId;

    /** 商品名称关键字 */
    private String name;

    /** 一级类目 ID */
    private Long cateId1;

    /** 二级类目 ID */
    private Long cateId2;

    /** 三级类目 ID */
    private Long cateId3;

    /** 渠道类型列表 */
    private List<Integer> channelTypes;
}
