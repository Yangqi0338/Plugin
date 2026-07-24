package com.newzkl.platform.plugin.operator.goods;

import com.newzkl.platform.base.biz.goods.application.goods.service.goods.GoodsQueryService;
import com.newzkl.platform.base.biz.goods.application.goods.ext.SpuQueryExt;
import com.newzkl.platform.base.biz.goods.model.enums.user.identity.RoleEnum;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.application.spi.IdentityImpl;
import lombok.RequiredArgsConstructor;

/**
 * 商品详情查询-运营商实现。
 *
 * <p>命中运营商身份, 以运营商可见口径复用商品查询能力。
 * TODO[identity-scope]: v1 先直调查询证分发, 运营商辖域 scope 差异后续补。
 * TODO[enum-dedup] #91: 条件码取 biz-goods RoleEnum.CompanyRole.OPERATOR, 注解需编译期常量故用字面量。</p>
 *
 * @author KC
 */
@IdentityImpl(1004L)
@RequiredArgsConstructor
public class OperatorSpuQueryExt implements SpuQueryExt {

    static {
        assert RoleEnum.CompanyRole.OPERATOR.getCode() == 1004L : "CompanyRole OPERATOR code 漂移";
    }

    private final GoodsQueryService goodsQueryService;

    @Override
    public SpuVO spu(Long id, Boolean needExtraInfo) {
        return goodsQueryService.spuVO(id, needExtraInfo);
    }
}
