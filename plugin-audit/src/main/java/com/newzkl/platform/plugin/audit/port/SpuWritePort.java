package com.newzkl.platform.plugin.audit.port;

import com.newzkl.platform.plugin.audit.workflow.model.SpuUpdateResult;

import java.util.List;

/**
 * SPU 写入出站端口
 *
 * <p>审批通过后回写商品主体, 实现落 adapter 层转调 biz-goods SpuDomain</p>
 *
 * @author KC
 */
public interface SpuWritePort {

    /**
     * 更新 SPU
     *
     * @param spuCommandJson SPU 变更命令 JSON, 结构同 biz-goods SpuDTO
     * @return 更新结果, 含新增/更新/删除的 skuId 列表
     */
    SpuUpdateResult spuUpdate(String spuCommandJson);

    /**
     * 批量上下架
     *
     * @param enable 1 上架 0 下架
     * @param spuIdList SPU 主键列表
     * @return 影响行数
     */
    int spuUp(Integer enable, List<Long> spuIdList);
}
