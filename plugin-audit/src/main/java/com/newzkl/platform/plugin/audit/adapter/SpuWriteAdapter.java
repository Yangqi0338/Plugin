package com.newzkl.platform.plugin.audit.adapter;

import com.alibaba.fastjson2.JSON;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.biz.goods.model.goods.dto.spu.SpuDTO;
import com.newzkl.platform.base.biz.goods.model.goods.res.spu.SpuUpdateRes;
import com.newzkl.platform.plugin.audit.port.SpuWritePort;
import com.newzkl.platform.plugin.audit.workflow.model.SpuUpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SPU 写入适配器
 *
 * <p>转调 biz-goods SpuDomain 回写商品主体, 边界处做 JSON 反序列化与结果类型转换, 隔离 Base 内部 SpuDTO/SpuUpdateRes</p>
 *
 * @author KC
 */
@Component("auditPluginSpuWriteAdapter")
@RequiredArgsConstructor
public class SpuWriteAdapter implements SpuWritePort {

    private final SpuDomain spuDomain;

    @Override
    public SpuUpdateResult spuUpdate(String spuCommandJson) {
        SpuDTO spuDTO = JSON.parseObject(spuCommandJson, SpuDTO.class);
        SpuUpdateRes res = spuDomain.spuUpdate(spuDTO);
        return new SpuUpdateResult(res.getAddSkuIdList(), res.getUpdateSkuIdList(), res.getDeleteSkuIdList());
    }

    @Override
    public int spuUp(Integer enable, List<Long> spuIdList) {
        return spuDomain.spuUp(enable, spuIdList);
    }
}
