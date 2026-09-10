package com.newzkl.platform.plugin.hdh;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.biz.vo.CategoryLayerVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.RoleLimit;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 货盘选品控制器
 *
 * <p>迁自 new-scm scm-goods SpuController(palletSpuPage/palletSpu) + CategoryController(palletCategoryList),
 * URL 逐字不变。会订货外链能力聚于本插件, 从 biz-goods 迁出</p>
 *
 * <p>⚠️ 出参契约: 源 palletSpuPage 返 PageHelper PageInfo, 本仓按 rules/Architecture.md 改
 * MyBatis-Plus Page; palletCategoryList 源用 ScmUtil.listToTree 二次建树, 本实现在
 * buildCategoryVOByHdhCategory 内递归子树, 直返顶层列表</p>
 *
 * @author KC
 */
// TODO[auth-defer]: 源 @Limit(FuncCons.goods...) 待入口 starter 鉴权切面接入 (#178)
@RestController("hdhPalletController")
@RequestMapping("/goods")
@RequiredArgsConstructor
public class PalletController {

    private final PalletGoodsService palletGoodsService;

    /**
     * 货盘 SPU 分页 (仅平台角色)
     *
     * @param spuQuery 查询条件
     * @return SPU 分页
     */
    @RoleLimit(client = {AccountEnum.Client.ADMIN, AccountEnum.Client.MMT_CHANNEL})
    @PostMapping("/spu/palletSpuPage")
    public PlatformResult<Page<SpuVO>> palletSpuPage(@RequestBody @Validated com.newzkl.platform.plugin.hdh.model.PalletSpuQuery spuQuery) {
        return PlatformResult.success(palletGoodsService.palletSpuPage(spuQuery));
    }

    /**
     * 货盘 SPU 详情 (仅平台角色)
     *
     * @param spuQuery 查询条件 (需 outSpuId)
     * @return SPU 详情
     */
    @RoleLimit(client = {AccountEnum.Client.ADMIN, AccountEnum.Client.MMT_CHANNEL})
    @PostMapping("/spu/palletSpu")
    public PlatformResult<SpuVO> palletSpu(@RequestBody com.newzkl.platform.plugin.hdh.model.PalletSpuQuery spuQuery) {
        return PlatformResult.success(palletGoodsService.palletSpuDetail(spuQuery));
    }

    /**
     * 货盘分类树
     *
     * @param categoryQuery 查询条件
     * @return 分类树
     */
    @RoleLimit(client = {AccountEnum.Client.ADMIN, AccountEnum.Client.MMT_CHANNEL})
    @PostMapping("/category/palletCategoryList")
    public PlatformResult<List<CategoryLayerVO>> palletCategoryList(@RequestBody @Validated com.newzkl.platform.plugin.hdh.model.PalletCategoryQuery categoryQuery) {
        return PlatformResult.success(palletGoodsService.palletCategoryList(categoryQuery));
    }
}
