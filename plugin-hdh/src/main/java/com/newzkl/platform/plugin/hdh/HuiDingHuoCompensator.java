package com.newzkl.platform.plugin.hdh;


import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.biz.order.domain.spi.ThirdPartyCompensator;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import com.newzkl.platform.plugin.hdh.model.req.HuiDingHuoCreateOrderReq;
import com.newzkl.platform.plugin.hdh.model.res.HuiDingHuoCreateOrderRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 惠订货补偿器(补偿轴)
 *
 * <p>重推 {@link HuiDingHuoOrderStrategy#create} 失败落库的下单记录 只负责发一次三方请求
 * 状态与动作日志由派发器 {@code ThirdPartyOrderProcessor.compensation} 统一回写</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HuiDingHuoCompensator implements ThirdPartyCompensator {

	@Override
	public void compensation(ThirdPartyOrderRecordDTO request) {
		log.info("开始补偿惠订货订单: {}", request.getBizOrderNo());
		// 只有下单记录可重推 其余动作(补偿日志/开发者通知)不属惠订货补偿范围
		if (!ThirdPartyOrderEnum.Action.CREATE.equals(request.getInterfaceName())) {
			log.info("非下单动作记录 跳过惠订货补偿 interfaceName={}", request.getInterfaceName());
			return;
		}
		try {
			// 反序列化原下单请求参数
			HuiDingHuoCreateOrderReq createOrderReq = JSONObject.parseObject(request.getRequestJson(), HuiDingHuoCreateOrderReq.class);
			// 重新调用三方下单接口
			HuiDingHuoCreateOrderRes order = HuiDingHuoApiUtils.createOrder(createOrderReq);
			// 结果回写进 request 由派发器落动作日志
			request.setResponseJson(JSONObject.toJSONString(order));
			request.setRequestStatus(CommonEnum.RequestStatusEnum.getByCode(order.getSuccess()));
			request.setErrorMessage(order.getMessage());
			log.info("惠订货订单补偿 id: {} , 补偿结果: {}", request.getBizOrderNo(), order.getCode());
		} catch (Exception e) {
			request.setRequestStatus(CommonEnum.RequestStatusEnum.FAILED);
			request.setErrorMessage(e.getMessage());
			log.error("惠订货订单补偿失败，调用API异常。订单号: {}", request.getBizOrderNo(), e);
		}
	}

	@Override
	public boolean supports(ThirdPartyOrderEnum.PlatformTypeEnum platformType) {
		return ThirdPartyOrderEnum.PlatformTypeEnum.HUI_DING_HUO == platformType;
	}
}
