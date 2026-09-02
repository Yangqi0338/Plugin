package com.newzkl.platform.plugin.audit.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuAttributeVO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.plugin.audit.model.dto.SkuAuditDTO;
import com.newzkl.platform.plugin.audit.model.dto.SpuAuditDTO;
import com.newzkl.platform.plugin.audit.model.dto.SpuWorkTableDTO;
import com.newzkl.platform.plugin.audit.model.query.SpuWorkTableQuery;

import java.util.List;
import java.util.Map;

/**
 * SPU 工单审批领域服务
 *
 * <p>SPU 工单审批第二线内联落地: 各类变更提交经专用方法组工单落库为待审核, 平台审核态直接落本表。
 * 提交编排(逐 SPU 组工单)与状态流转内联本域; SPU 主数据真实变更由应用层 SpuWorkTableService
 * 审批通过时直接调用 biz-goods SpuDomain 执行, 无审批流引擎、无 MQ 回调, 与 PromiseFlow 内联模式对齐。</p>
 *
 * @author KC
 */
public interface SpuWorkTableDomain {

    /**
     * 提交 SPU 基础信息修改工单
     *
     * @param spuVO SPU 变更主体, 含 id
     */
    void submitSpuBaseUpdate(SpuAuditDTO spuVO);

    /**
     * 提交 SKU 基础信息修改工单
     *
     * @param spuId SPU 主键
     * @param skuList 待改 SKU 列表, 为待新增 SKU 补 tempId
     */
    void submitSkuBaseUpdate(Long spuId, List<SkuAuditDTO> skuList);

    /**
     * 提交 SPU 状态修改工单
     *
     * <p>spuIdList 批量, 每 SPU 一工单</p>
     *
     * @param spuIdList SPU 主键列表
     * @param enable 是否上架, YES 置上架 NO 置下架
     */
    void submitSpuStateUpdate(List<Long> spuIdList, CommonEnum.YesOrNo enable);

    /**
     * 提交规格删除工单
     *
     * @param spuId SPU 主键
     * @param spuSaleAttributeList 删除后保留的销售属性列表
     */
    void submitSaleAttributeDelete(Long spuId, List<SpuAttributeVO> spuSaleAttributeList);

    /**
     * 提交规格新增工单
     *
     * <p>提交前做笛卡尔积校验: 用旧 SPU 快照与新销售属性连乘差值比对当前待新增 SKU 数,
     * 校验通过再为待新增 SKU 补 tempId 并落工单</p>
     *
     * @param spuId SPU 主键
     * @param skuList 新增 SKU 列表
     * @param spuSaleAttributeList 新增后完整销售属性列表
     */
    void submitSaleAttributeAdd(Long spuId, List<SkuAuditDTO> skuList, List<SpuAttributeVO> spuSaleAttributeList);

    /**
     * 工单审批分页
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<SpuWorkTableDTO> workTablePage(SpuWorkTableQuery query);

    /**
     * 工单审批详情
     *
     * @param workTableId 工单主键
     * @return 工单审批数据
     */
    SpuWorkTableDTO workTable(Long workTableId);

    /**
     * 置工单为审核通过
     *
     * <p>置审核态为通过, skuSalePrice 非空时落为审核价快照。越态校验与 SPU 主数据真实变更
     * 由应用层 SpuWorkTableService 在同事务内先完成, 本方法仅更新工单状态。</p>
     *
     * @param workTableId 工单主键
     * @param skuSalePrice 审核录入的 SKU 销售价快照, 可空
     */
    void markPass(Long workTableId, Map<String, String> skuSalePrice);

    /**
     * 工单审批拒绝
     *
     * <p>仅待审核单可拒绝。置审核态为未通过并记录拒绝原因</p>
     *
     * @param workTableId 工单主键
     * @param refuseReason 拒绝原因
     */
    void refuse(Long workTableId, String refuseReason);
    
    /**
     * 工单审批停止
     *
     * @param workTableId 工单主键
     * @param reason 拒绝原因
     */
    void stop(Long workTableId, String reason);
}
