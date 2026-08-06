package com.newzkl.platform.plugin.audit.port;

/**
 * SPU提交出站端口
 *
 * <p>审批流提交后回写biz-goods spuSubmit状态, 实现落adapter层转调biz-goods SpuDomain</p>
 *
 * @author KC
 */
public interface SpuSubmitPort {

    /**
     * 提交SPU进入待审核状态
     *
     * @param spuId SPU主键
     * @param flowId 审批流ID
     */
    void spuSubmit(Long spuId, Long flowId);
}
