package com.newzkl.platform.plugin.hdh.task;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ThirdPartyOrderRepository;
import com.newzkl.platform.base.biz.order.domain.spi.ThirdPartyOrderProcessor;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.ThirdPartyOrderDTO;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 会订货三方订单补偿任务
 *
 * <p>迁移自 {@code com.zkl.scm.sale.application.task.ThirdPartyOrderJobHandler}。
 * 只处理会订货(HUI_DING_HUO)平台的失败订单补偿。</p>
 *
 * @author sijiwang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ThirdPartyOrderJobHandler {

    private static final String SUCCESS = "SUCCESS";

    private final ThirdPartyOrderRepository thirdPartyOrderRepository;

    @XxlJob("createOrder")
    public ReturnT<String> create(String param) {
        log.info("XXL Job任务[createOrder]开始执行，参数：{}", param);
        try {
            // 只查询会订货平台的失败订单
            List<ThirdPartyOrderRecordDTO> compensationList =
                    thirdPartyOrderRepository.findByStatusAndNextRetryTimeBefore(
                            PlatformTypeEnum.HUI_DING_HUO,
                            CommonEnum.RequestStatusEnum.FAILED,
                            "create");
            log.info("获取到需要补偿的失败订单数量：{}", compensationList.size());

            if (CollUtil.isEmpty(compensationList)) {
                log.info("无需要补偿的订单，任务提前结束");
                return new ReturnT<>(SUCCESS);
            }

            for (ThirdPartyOrderRecordDTO record : compensationList) {
                log.info("开始处理订单补偿，业务订单号：{}", record.getBizOrderNo());
                try {
                    ThirdPartyOrderProcessor processor = ThirdPartyOrderProcessor.find();
                    ThirdPartyOrderDTO dto = TransferUtils.transfer(record, ThirdPartyOrderDTO.class);
                    processor.compensation(dto);
                    log.info("订单补偿逻辑执行完成，业务订单号：{}", record.getBizOrderNo());
                } catch (Exception e) {
                    log.error("处理订单补偿时发生异常，业务订单号：{}", record.getBizOrderNo(), e);
                }
            }

            log.info("XXL Job任务[createOrder]执行完成，共处理订单数量：{}", compensationList.size());
        } catch (Exception e) {
            log.error("XXL Job任务[createOrder]执行失败", e);
            return ReturnT.FAIL;
        }
        return new ReturnT<>(SUCCESS);
    }
}