package com.newzkl.platform.plugin.bi.domain.service;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;

import java.math.BigDecimal;

/**
 * BI 事件写入领域服务
 *
 * <p>MQ Consumer 统一入口: 业务事件 → 写实时宽表(补偿增量/流量)。
 * 宽表字段 = 查询展示字段, 事件落库时算好, 查询不碰事实表。</p>
 */
public interface AdminEventDomain {

    /** 支付成功(任何支付): OverviewDO.gmv/payOrderCount + PaymentSummaryDO.payAmount */
    void onPaySuccess(CommonEnum.Client client, Long userId, BigDecimal amount);

    /** 商品订单支付成功: TradeDO + TodoDO + 排行 + CorrelationDO */
    void onGoodsPaySuccess(CommonEnum.Client client, Long userId, Long storeId, Long goodsId, BigDecimal amount);

    /** 退款通过: TodoDO(售后中+1) */
    void onRefundPass(CommonEnum.Client client, Long userId, BigDecimal refundAmount);

    // ==================== 待接入 tag(事件类已定义, Consumer 待 tag) ====================

    /** 会员注册: MemberSummaryDO.memberCount+1 */
    void onMemberRegister(CommonEnum.Client client, Long memberId, String level);

    /** 上链存证: OverviewDO.evidenceCount+1 + TradeDO.onChainCount+1 */
    void onEvidenceOnChain(CommonEnum.Client client, Long evidenceId, Long userId);

    /** 库存变更: TodoDO.stockWarnDelta/soldOutDelta(阈值 BiProperties.stockWarnRatio) */
    void onInventoryChange(CommonEnum.Client client, Long goodsId, Long storeId, String status);

    /** 商品状态变更: GoodsStatusDO.onShelfCount/offShelfCount */
    void onGoodsStatus(CommonEnum.Client client, Long goodsId, String status);
}
