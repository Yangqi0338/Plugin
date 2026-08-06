package com.newzkl.platform.plugin.audit.port;

/**
 * 商品位出站端口
 *
 * <p>审批流提交SPU时扣减商品位, 实现落adapter层转调biz-finance GoodsSeatFacade</p>
 *
 * @author KC
 */
public interface GoodsSeatPort {

    /**
     * 供应商提交SPU审核时扣减1个商品位
     *
     * @param supplierId 供应商ID
     * @param spuId SPU主键
     */
    void supplierSubmitSubGoodsSeat(Long supplierId, Long spuId);
}
