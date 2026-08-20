package com.newzkl.platform.plugin.rule_template.application.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.store.domain.adapt.api.DistributionApi;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreDomain;
import com.newzkl.platform.base.biz.store.domain.store.service.StoreStyleDomain;
import com.newzkl.platform.base.biz.store.domain.template.service.ModelShopDomain;
import com.newzkl.platform.base.biz.store.domain.template.service.ModelShopUseRecordDomain;
import com.newzkl.platform.base.biz.store.model.store.entity.Store;
import com.newzkl.platform.base.biz.store.model.store.entity.StoreStyle;
import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopDTO;
import com.newzkl.platform.base.biz.store.model.template.dto.ModelShopUseRecordDTO;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.plugin.rule_template.application.service.ModelShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 样板店应用服务实现
 *
 * @author niu
 */
@Service
@RequiredArgsConstructor
public class ModelShopServiceImpl implements ModelShopService {

    private final StoreDomain storeDomain;
    private final StoreStyleDomain storeStyleDomain;
    private final ModelShopDomain modelShopDomain;
    private final ModelShopUseRecordDomain modelShopUseRecordDomain;
    private final DistributionApi distributionApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void useModelShop(String styleCode) {
        Store store = storeDomain.store(SecurityUtils.getAccountId());
        if (store == null) {
            throw new PlatformException(-1, "门店不存在");
        }
        disposeOldModelShop(store);
        if (styleCode == null) {
            handleCancelModelShop(store);
            return;
        }
        handleApplyModelShop(store, styleCode);
    }

    private void handleCancelModelShop(Store store) {
        storeDomain.cancelModelShop();
        store.setModelShopId(null);
        StoreStyle storeStyle = storeStyleDomain.copyStyle(null);
        store.setStyleCode(storeStyle.getStyleCode());
        storeDomain.storeUpdate(store);
    }

    private void handleApplyModelShop(Store store, String targetStyleCode) {
        Set<Long> goodIdList = new HashSet<>();
        ModelShopDTO targetModelShop = modelShopDomain.queryByStyleCode(targetStyleCode);
        if (targetModelShop == null) {
            throw new IllegalArgumentException("样板店不存在: " + targetStyleCode);
        }
//        modelShopDomain.updateUseStoreNum(targetStyleCode, 1);
        modelShopDomain.updateTotalUseStoreNum(store.getId(), targetModelShop.getId());
        store.setModelShopId(targetModelShop.getId());
        StoreStyle oneselfStyle = storeStyleDomain.getOneselfStyle(targetStyleCode);
        if (oneselfStyle == null) {
            StoreStyle copiedStyle = storeStyleDomain.copyStyle(targetStyleCode);
            store.setStyleCode(copiedStyle.getStyleCode());
            if (copiedStyle.getGoodsIdListStr() != null) {
                goodIdList = StrUtil.split(copiedStyle.getGoodsIdListStr(), ",").stream()
                        .map(Long::valueOf).collect(Collectors.toSet());
            }
        } else {
            store.setStyleCode(oneselfStyle.getStyleCode());
            goodIdList = StrUtil.split(oneselfStyle.getGoodsIdListStr(), ",").stream()
                    .map(Long::valueOf).collect(Collectors.toSet());
        }
        storeDomain.storeUpdate(store);
        if (CollUtil.isNotEmpty(goodIdList)) {
            synchronizeGoodsData(targetModelShop.getChannelId(), goodIdList);
        }
        ModelShopUseRecordDTO modelShopUseRecord = new ModelShopUseRecordDTO();
        modelShopUseRecord.setModelShopId(store.getModelShopId());
        modelShopUseRecord.setStoreId(store.getId());
        modelShopUseRecord.setStoreName(store.getName());
        modelShopUseRecord.setModelShopName(targetModelShop.getModelShopName());
        modelShopUseRecordDomain.create(modelShopUseRecord);
    }

    private void disposeOldModelShop(Store store) {
        if (store.getModelShopId() != null && store.getStyleCode() != null) {
            ModelShopDTO modelShop = modelShopDomain.queryById(store.getModelShopId());
//            if (modelShop != null) {
//                modelShopDomain.updateUseStoreNum(modelShop.getStyleCode(), -1);
//            }
            storeStyleDomain.deleteCopyStyle(store.getStyleCode());
        }
    }

    private void synchronizeGoodsData(Long targetChannelId, Set<Long> goodIdList) {
        distributionApi.batchCopyDistribution(targetChannelId, SecurityUtils.getAccountId(), goodIdList);
    }
}