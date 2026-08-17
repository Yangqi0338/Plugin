package com.newzkl.platform.plugin.channel.goods;

import com.newzkl.platform.base.biz.goods.application.goods.service.goods.GoodsQueryService;
import com.newzkl.platform.base.biz.goods.application.goods.ext.SpuQueryExt;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.ddd.application.spi.IdentityImpl;
import lombok.RequiredArgsConstructor;

/**
 * 商品详情查询-渠道商实现。
 *
 * <p>命中渠道商身份, 以渠道商可见口径复用商品查询能力。
 * TODO[identity-scope]: v1 先直调查询证分发, 渠道价/可见性 scope 差异后续补。
 * 条件码取 canonical RoleEnum.CompanyRole.CHANNEL, 注解需编译期常量故用字面量。</p>
 *
 * @author KC
 */
@IdentityImpl(RoleEnum.CompanyRole.CHANNEL)
@RequiredArgsConstructor
public class ChannelSpuQueryExt implements SpuQueryExt {

    static {
        assert RoleEnum.CompanyRole.CHANNEL.getCode() == 1002L : "CompanyRole CHANNEL code 漂移";
    }

    private final GoodsQueryService goodsQueryService;

    @Override
    public SpuVO spu(Long id, Boolean needExtraInfo) {
        return goodsQueryService.spuVO(id, needExtraInfo);
    }
}
