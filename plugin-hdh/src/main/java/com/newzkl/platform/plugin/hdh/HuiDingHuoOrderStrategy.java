package com.newzkl.platform.plugin.hdh;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderDomain;
import com.newzkl.platform.base.biz.order.domain.spi.ThirdPartyOrderStrategy;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.biz.order.model.dto.OrderDTO;
import com.newzkl.platform.base.biz.order.model.dto.SkuCountDTO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderSkuVO;
import com.newzkl.platform.base.biz.order.model.vo.ShipVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.facade.ThirdPartyOrderResult;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import com.newzkl.platform.base.common.ddd.model.properties.PalletProperties;
import com.newzkl.platform.plugin.hdh.model.req.HuiDingHuoCreateOrderReq;
import com.newzkl.platform.plugin.hdh.model.res.HuiDingHuoCreateOrderRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 惠订货第三方下单策略实现
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HuiDingHuoOrderStrategy implements ThirdPartyOrderStrategy {
	
	private final ThirdPartyOrderDomain thirdPartyOrderDomain;
	
	@Override
	public ThirdPartyOrderResult create(List<OrderSkuVO> outGoods, OrderDTO order) {
		// 构建惠订货下单请求
		HuiDingHuoCreateOrderReq req = buildHuiDingHuoCreateOrder(outGoods, order);
		// 调用惠订货接口下单
		HuiDingHuoCreateOrderRes result = HuiDingHuoApiUtils.createOrder(req);
		// 适配返回结果
		return new HuiDingHuoOrderResultAdapter(result, req);
	}
	
	@Override
	public ThirdPartyOrderResult delivery(String outOrderNo, List<SkuCountDTO> skuCountDTOList, String expressCompanyName, String expressNo, Long channelId) {
		return null;
	}
	
	@Override
	public void compensation(ThirdPartyOrderRecordDTO request) {
		log.info("开始补偿惠订货订单: {}", request.getBizOrderNo());
		if (!request.getInterfaceName().equals("create")) return;
		try {
			// 1. 反序列化请求参数 (假设是 HuiDingHuoCreateOrderReq)
			HuiDingHuoCreateOrderReq createOrderReq = JSONObject.parseObject(request.getRequestJson(), HuiDingHuoCreateOrderReq.class);
			
			// 2. 调用第三方API进行补偿
			HuiDingHuoCreateOrderRes order = HuiDingHuoApiUtils.createOrder(createOrderReq);
			
			// 3. 处理成功结果
			request.setResponseJson(JSONObject.toJSONString(order));
			request.setRequestStatus(CommonEnum.RequestStatusEnum.getByCode(order.getSuccess()));
			request.setErrorMessage(order.getMessage());
			// TODO
//			thirdPartyOrderDomain.recordAction(request.getPlatformType(), request.getBizOrderNo(),request.getBizOrderNo());
			log.info("惠订货订单补偿 id: {} , 补偿结果: {}", request.getBizOrderNo(), order.getCode());
		} catch (Exception e) {
			log.error("惠订货订单补偿失败，调用API异常。订单号: {}", request.getBizOrderNo(), e);
		}
	}

    /**
	 * 构建惠订货下单请求参数
	 */
	private HuiDingHuoCreateOrderReq buildHuiDingHuoCreateOrder(List<OrderSkuVO> outGoods, OrderDTO order) {
		if (outGoods == null || outGoods.isEmpty()) {
			return null;
		}

		// 创建请求对象
		HuiDingHuoCreateOrderReq req = new HuiDingHuoCreateOrderReq();

		// 设置收货信息
		ShipVO shipVO = order.getShipVO();
		if (shipVO != null) {
			req.setName(shipVO.getShipName());
			req.setPhone(shipVO.getShipPhone());
			// 解析地区信息（格式：省,市,区）
			String[] areas = shipVO.getShipArea().split("-");
			if (areas.length >= 3) {
				req.setProvince(areas[0]);
				req.setCity(areas[1]);
				req.setDistrict(areas[2]);
			}
			req.setAddress(shipVO.getShipAddress());
		}

		// 设置订单信息
		req.setUserOrderNum(String.valueOf(order.getId()));
        req.setPrice(order.getSupplierAmount().divide(new BigDecimal("100"))); // 金额单位转换
		req.setDesc(order.getRemark());

		// 构建商品列表
		List<HuiDingHuoCreateOrderReq.SkuItem> skuList = outGoods.stream().map(sku -> {
			HuiDingHuoCreateOrderReq.SkuItem item = new HuiDingHuoCreateOrderReq.SkuItem();
			item.setSkuId(sku.getOutId());
			item.setItemId(sku.getOutSpuId());
            item.setChannelType("2");
			item.setBuyNum(sku.getCount());
			return item;
		}).collect(Collectors.toList());
		req.setSkuList(skuList);
		
        // 测试商品收货人名不为空, 且当前订单收件人=测试商品收件人
         if (StrUtil.isNotBlank(PalletProperties.testSpuShipName) && PalletProperties.testSpuShipName.equals(req.getName())) {
             req.getSkuList().forEach(sku -> {
                 sku.setItemId(null);
                 sku.setItemCode(HuiDingHuoApiUtils.PROD_ITEM_CODE);
                 sku.setChannelType("2");
                 sku.setBuyNum(2);
             });
             req.setPrice(Money.of("2"));
             log.info("生产代理商品-" + JSONUtil.toJsonStr(req));
         }
		return req;
	}
	
	@Override
	public boolean supports(Object type) {
		return ThirdPartyOrderEnum.PlatformTypeEnum.HUI_DING_HUO == type;
	}
}