package com.newzkl.platform.plugin.bi.domain.service;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.plugin.bi.domain.constant.BiEventType;
import com.newzkl.platform.plugin.bi.infrastructure.entity.BIBaseDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.CorrelationDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.GoodsRankDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.GoodsStatusDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.MemberSummaryDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.OverviewDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.PaymentSummaryDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.StoreRankDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.TodoDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.TradeDO;
import com.newzkl.platform.plugin.bi.infrastructure.entity.admin.GoodsSummaryDO;
import com.newzkl.platform.plugin.bi.domain.repository.StatRealtimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * BI 事件写入服务
 *
 * <p>MQ Consumer 统一入口: 业务事件 → 写实时宽表(补偿增量/流量)。
 * 宽表字段 = 查询展示字段, 事件落库时算好, 查询不碰事实表。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BiEventWriteService {

    private final StatRealtimeRepository realtimeRepo;

    // ==================== 真实事件(已接 tag) ====================

    /** 支付成功(任何支付): OverviewDO.gmv/payOrderCount + PaymentSummaryDO.payAmount */
    public void onPaySuccess(CommonEnum.Client client, Long userId, BigDecimal amount) {
        OverviewDO overview = new OverviewDO();
        fillBase(overview, client, userId, BiEventType.ORDER_PAY);
        overview.setGmv(amount);
        overview.setPayOrderCount(1);
        realtimeRepo.insert(overview);

        PaymentSummaryDO payment = new PaymentSummaryDO();
        fillBase(payment, client, userId, BiEventType.PAYMENT_PAY);
        payment.setPayAmount(amount);
        realtimeRepo.insert(payment);
    }

    /** 商品订单支付成功: TradeDO + TodoDO + 排行 + CorrelationDO */
    public void onGoodsPaySuccess(CommonEnum.Client client, Long userId, Long storeId, Long goodsId, BigDecimal amount) {
        TradeDO trade = new TradeDO();
        fillBase(trade, client, userId, BiEventType.ORDER_PAY);
        trade.setAmount(amount);
        trade.setOnChainCount(0);
        realtimeRepo.insert(trade);

        TodoDO todo = new TodoDO();
        fillBase(todo, client, userId, BiEventType.ORDER_PAY);
        todo.setWaitPayDelta(-1);
        todo.setWaitDeliveryDelta(1);
        realtimeRepo.insert(todo);

        if (goodsId != null) {
            GoodsRankDO rank = new GoodsRankDO();
            fillBase(rank, client, userId, BiEventType.ORDER_PAY);
            rank.setGoodsId(goodsId);
            rank.setSalesCount(1);
            rank.setSalesAmount(amount);
            realtimeRepo.insert(rank);
        }

        if (storeId != null) {
            StoreRankDO storeRank = new StoreRankDO();
            fillBase(storeRank, client, userId, BiEventType.ORDER_PAY);
            storeRank.setStoreId(storeId);
            storeRank.setSalesAmount(amount);
            realtimeRepo.insert(storeRank);
        }

        CorrelationDO corr = new CorrelationDO();
        fillBase(corr, client, userId, BiEventType.ORDER_PAY);
        corr.setAmount(amount);
        corr.setOrderCount(1);
        corr.setOnChainCount(0);
        realtimeRepo.insert(corr);
    }

    /** 退款通过: TodoDO(售后中+1) */
    public void onRefundPass(CommonEnum.Client client, Long userId, BigDecimal refundAmount) {
        TodoDO todo = new TodoDO();
        fillBase(todo, client, userId, BiEventType.ORDER_REFUND);
        todo.setRefundingDelta(1);
        realtimeRepo.insert(todo);
        log.info("退款通过: 金额 {}, 售后中+1", refundAmount);
    }

    // ==================== 模拟事件(BiTriggerConsumer 分发) ====================

    /** 会员注册: MemberSummaryDO.memberCount+1 */
    public void onMemberRegister(CommonEnum.Client client, Long memberId, String level) {
        MemberSummaryDO member = new MemberSummaryDO();
        fillBase(member, client, memberId, BiEventType.MEMBER_REGISTER);
        member.setMemberCount(1);
        realtimeRepo.insert(member);
    }

    /** 上链存证: OverviewDO.evidenceCount+1 + TradeDO.onChainCount+1 */
    public void onEvidenceOnChain(CommonEnum.Client client, Long evidenceId, Long userId) {
        OverviewDO overview = new OverviewDO();
        fillBase(overview, client, userId, BiEventType.EVIDENCE_ON_CHAIN);
        overview.setEvidenceCount(1);
        realtimeRepo.insert(overview);

        TradeDO trade = new TradeDO();
        fillBase(trade, client, userId, BiEventType.EVIDENCE_ON_CHAIN);
        trade.setOnChainCount(1);
        realtimeRepo.insert(trade);
    }

    /** 库存变更: TodoDO.stockWarnDelta/soldOutDelta(按 amount 表示库存比例, level 存状态) */
    public void onInventoryChange(CommonEnum.Client client, Long goodsId, Long storeId, BigDecimal stockRatio, String status) {
        TodoDO todo = new TodoDO();
        fillBase(todo, client, null, BiEventType.INVENTORY_CHANGE);
        if ("SOLD_OUT".equals(status)) {
            todo.setSoldOutDelta(1);
        } else if ("WARN".equals(status)) {
            todo.setStockWarnDelta(1);
        }
        realtimeRepo.insert(todo);
    }

    /** 商品状态: GoodsStatusDO.onShelfCount/offShelfCount(审核态待扩展) */
    public void onGoodsStatus(CommonEnum.Client client, String status) {
        GoodsStatusDO goods = new GoodsStatusDO();
        fillBase(goods, client, null, BiEventType.GOODS_ONLINE);
        if ("ON_SHELF".equals(status)) {
            goods.setOnShelfCount(1);
        } else if ("OFF_SHELF".equals(status)) {
            goods.setOffShelfCount(1);
        }
        realtimeRepo.insert(goods);
    }

    /** 订单创建: TodoDO.waitPayDelta+1 */
    public void onOrderCreate(CommonEnum.Client client, Long userId) {
        TodoDO todo = new TodoDO();
        fillBase(todo, client, userId, BiEventType.ORDER_CREATE);
        todo.setWaitPayDelta(1);
        realtimeRepo.insert(todo);
    }

    /** 订单支付(回查金额): 调 onGoodsPaySuccess, 金额由回查填充 */
    public void onOrderPay(CommonEnum.Client client, Long orderId) {
        // TODO: 回查订单金额(orderId -> amount), 待 Base 提供订单查询端口
        log.warn("订单支付回查待接入, orderId: {}", orderId);
        // onGoodsPaySuccess(client, null, null, null, amount);
    }

    // ==================== 私有 ====================

    private void fillBase(BIBaseDO entity, CommonEnum.Client client, Long userId, BiEventType eventType) {
        entity.setClient(client);
        entity.setUserId(userId);
        entity.setEventType(eventType);
        entity.setEventTime(LocalDateTime.now());
    }
}
