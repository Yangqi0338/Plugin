package com.newzkl.platform.plugin.audit.port;

/**
 * SPU 读取出站端口
 *
 * <p>审批流程读取商品快照, 实现落 adapter 层转调 biz-goods GoodsQueryService</p>
 *
 * @author KC
 */
public interface SpuReadPort {

    /**
     * 查 SPU 详情 JSON
     *
     * @param spuId SPU 主键
     * @return SPU 详情 JSON, 结构同 biz-goods SpuVO
     */
    String spuInfoJson(Long spuId);

    /**
     * 查 SPU 名称
     *
     * @param spuId SPU 主键
     * @return SPU 名称
     */
    String spuName(Long spuId);
}
