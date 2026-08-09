package com.newzkl.platform.plugin.bi.application.service;

import cn.hutool.core.date.DateUtil;
import com.newzkl.platform.base.biz.account.application.service.UserQueryService;
import com.newzkl.platform.base.biz.account.domain.adapt.api.FinancePurseApi;
import com.newzkl.platform.base.biz.account.domain.adapt.api.PurseAmountRes;
import com.newzkl.platform.base.biz.account.domain.service.ChannelClientDomain;
import com.newzkl.platform.base.biz.account.domain.service.SupplierClientDomain;
import com.newzkl.platform.base.biz.account.model.req.CountSaleQuery;
import com.newzkl.platform.base.biz.account.model.res.CountSaleVO;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderRepository;
import com.newzkl.platform.base.biz.order.model.req.query.SpuOrderQuery;
import com.newzkl.platform.base.common.ddd.facade.AccountPurseReq;
import com.newzkl.platform.base.common.ddd.facade.GoodsCountVO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.plugin.bi.model.res.SupplierIndexVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 首页统计编排服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IndexService {

    private final SupplierClientDomain supplierClientDomain;
    private final ChannelClientDomain channelClientDomain;
    private final UserQueryService userQueryService;
    private final SpuDomain spuDomain;
    private final OrderRepository orderRepository;
    private final FinancePurseApi financePurseApi;

    public SupplierIndexVO supplierIndex() {
        Long accountId = SecurityUtils.getAccountId();
        SupplierIndexVO vo = new SupplierIndexVO();
        var supplier = supplierClientDomain.supplier(accountId);
        vo.setTotalOrderNumber(supplier.getTotalOrderNumber());
        vo.setTotalOrderAmount(toInt(supplier.getTotalOrderAmount()));
        vo.setTotalRefundNumber(supplier.getTotalRefundNumber());
        vo.setTotalRefundAmount(toInt(supplier.getTotalRefundAmount()));
        fillTodayYesterday(vo, accountId, RoleEnum.CompanyRole.SUPPLIER.getCode());
        fillSaleCount(vo, orderRepository.stateCountMap(bySupplier(accountId)));
        GoodsCountVO goodsCount = spuDomain.goodsCountVO(accountId);
        vo.setStockWarn(goodsCount == null ? 0 : goodsCount.getStockWarn());
        vo.setStockEmpty(goodsCount == null ? 0 : goodsCount.getStockEmpty());
        vo.setWaitAudit(0);
        vo.setPurchaseBalance(null);
        vo.setGoodsSeat(null);
        vo.setTodayIncomeAmount(null);
        vo.setYesterdayIncomeAmount(null);
        vo.setCreateTime(LocalDateTime.now());
        return vo;
    }

    public SupplierIndexVO channelIndex() {
        Long accountId = SecurityUtils.getAccountId();
        SupplierIndexVO vo = new SupplierIndexVO();
        var channel = channelClientDomain.channel(accountId);
        vo.setTotalOrderNumber(channel.getTotalOrderNumber());
        vo.setTotalOrderAmount(toInt(channel.getTotalOrderAmount()));
        vo.setTotalRefundNumber(channel.getTotalRefundNumber());
        vo.setTotalRefundAmount(toInt(channel.getTotalRefundAmount()));
        fillTodayYesterday(vo, accountId, RoleEnum.CompanyRole.CHANNEL.getCode());
        fillSaleCount(vo, orderRepository.stateCountMap(byChannel(accountId)));
        vo.setStockWarn(0);
        vo.setStockEmpty(0);
        vo.setWaitAudit(0);
        AccountPurseReq req = new AccountPurseReq();
        req.setAccountId(accountId);
        req.setPurseType(0);
        vo.setPurchaseBalance(financePurseApi.queryPurse(req).stream().findFirst()
                .map(PurseAmountRes::getEarnings).orElse(null));
        req.setPurseType(1);
        vo.setGoodsSeat(financePurseApi.queryPurse(req).stream().findFirst()
                .map(PurseAmountRes::getEarnings).orElse(null));
        vo.setTodayIncomeAmount(null);
        vo.setYesterdayIncomeAmount(null);
        vo.setCreateTime(LocalDateTime.now());
        return vo;
    }

    private void fillTodayYesterday(SupplierIndexVO vo, Long accountId, Long roleCode) {
        LocalDateTime today = DateUtil.toLocalDateTime(DateUtil.beginOfDay(DateUtil.date()));
        fillDay(vo, accountId, roleCode, today, true);
        LocalDateTime yesterday = DateUtil.toLocalDateTime(DateUtil.beginOfDay(DateUtil.offsetDay(DateUtil.date(), -1)));
        fillDay(vo, accountId, roleCode, yesterday, false);
    }

    private void fillDay(SupplierIndexVO vo, Long accountId, Long roleCode, LocalDateTime day, boolean isToday) {
        CountSaleQuery q = new CountSaleQuery();
        q.setAccountId(accountId);
        q.setRole(roleCode);
        q.setDate(day);
        CountSaleVO cv = getFirst(userQueryService.countSalePage(q).getRecords());
        if (cv == null) return;
        if (isToday) {
            vo.setTodayOrderNumber(cv.getTotalOrderNumber());
            vo.setTodayRefundNumber(cv.getTotalRefundNumber());
            vo.setTodayOrderAmount(toInt(cv.getTotalOrderAmount()));
            vo.setTodayRefundAmount(toInt(cv.getTotalRefundAmount()));
        } else {
            vo.setYesterdayOrderNumber(cv.getTotalOrderNumber());
            vo.setYesterdayRefundNumber(cv.getTotalRefundNumber());
            vo.setYesterdayOrderAmount(toInt(cv.getTotalOrderAmount()));
            vo.setYesterdayRefundAmount(toInt(cv.getTotalRefundAmount()));
        }
    }

    private void fillSaleCount(SupplierIndexVO vo, Map<OrderEnum.State, Integer> stateCountMap) {
        if (stateCountMap == null) return;
        for (Map.Entry<OrderEnum.State, Integer> entry : stateCountMap.entrySet()) {
            if (entry.getKey() == null) continue;
            int code = entry.getKey().getCode();
            int count = entry.getValue() == null ? 0 : entry.getValue();
            if (code == OrderEnum.State.NEW.getCode() || code == OrderEnum.State.MEMBER_WAIT_PAY.getCode()
                    || code == OrderEnum.State.CHANNEL_WAIT_PAY.getCode() || code == OrderEnum.State.OPERATOR_WAIT_PAY.getCode()
                    || code == OrderEnum.State.SENDING.getCode()) {
                vo.setWaitPayNumber((vo.getWaitPayNumber() == null ? 0 : vo.getWaitPayNumber()) + count);
            }
            if (code == OrderEnum.State.WAIT_DELIVERY.getCode()) {
                vo.setWaitDeliveryNumber((vo.getWaitDeliveryNumber() == null ? 0 : vo.getWaitDeliveryNumber()) + count);
            }
            if (code == OrderEnum.State.REFUNDING.getCode()) {
                vo.setRefundPage((vo.getRefundPage() == null ? 0 : vo.getRefundPage()) + count);
            }
        }
    }

    private static SpuOrderQuery bySupplier(Long supplierId) {
        SpuOrderQuery q = new SpuOrderQuery();
        q.setSupplierId(supplierId);
        return q;
    }

    private static SpuOrderQuery byChannel(Long channelId) {
        SpuOrderQuery q = new SpuOrderQuery();
        q.setChannelId(channelId);
        return q;
    }

    private static <T> T getFirst(List<T> list) {
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    private static Integer toInt(com.newzkl.platform.base.common.core.model.money.Money money) {
        return money == null ? null : (int) money.getCent();
    }
}