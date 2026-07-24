package com.newzkl.platform.plugin.supplier.goods;

import com.newzkl.platform.base.biz.goods.application.goods.service.goods.GoodsQueryService;
import com.newzkl.platform.base.biz.goods.application.goods.ext.SpuQueryExt;
import com.newzkl.platform.base.biz.goods.model.enums.user.identity.RoleEnum;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.application.spi.IdentityImpl;
import lombok.RequiredArgsConstructor;

/**
 * 商品详情查询-供应商实现。
 *
 * <p>命中供应商身份, 以供应商可见口径复用商品查询能力。
 * TODO[identity-scope]: v1 先直调查询证分发, 供应商自家 spu scope 差异后续补。
 * TODO[enum-dedup] #91: 条件码取 biz-goods RoleEnum.CompanyRole.SUPPLIER, 注解需编译期常量故用字面量。</p>
 *
 * @author KC
 */
@IdentityImpl(1001L)
@RequiredArgsConstructor
public class SupplierSpuQueryExt implements SpuQueryExt {

    static {
        assert RoleEnum.CompanyRole.SUPPLIER.getCode() == 1001L : "CompanyRole SUPPLIER code 漂移";
    }

    private final GoodsQueryService goodsQueryService;

    @Override
    public SpuVO spu(Long id, Boolean needExtraInfo) {
        return goodsQueryService.spuVO(id, needExtraInfo);
    }
}
