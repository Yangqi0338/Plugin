package com.newzkl.platform.plugin.hdh;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.order.domain.spi.ThirdPartyOrderStrategy;
import com.newzkl.platform.base.biz.order.model.dto.OrderDTO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderSkuVO;
import com.newzkl.platform.base.common.ddd.model.vo.ShipVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.facade.ThirdPartyOrderResult;
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
 * 惠订货第三方下单策略实现(下单轴 按商品级供货平台派发)
 *
 * <p>补偿重推是另一条轴 见 {@link HuiDingHuoCompensator}</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HuiDingHuoOrderStrategy implements ThirdPartyOrderStrategy {

	@Override
	public ThirdPartyOrderResult create(List<OrderSkuVO> outGoods, OrderDTO order) {
		// 构建惠订货下单请求
		HuiDingHuoCreateOrderReq req = buildHuiDingHuoCreateOrder(outGoods, order);
		// 调用惠订货接口下单
		HuiDingHuoCreateOrderRes result = HuiDingHuoApiUtils.createOrder(req);
		// 适配返回结果
		return new HuiDingHuoOrderResultAdapter(result, req);
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