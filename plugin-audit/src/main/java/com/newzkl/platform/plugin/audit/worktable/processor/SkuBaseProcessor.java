package com.newzkl.platform.plugin.audit.worktable.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.plugin.audit.workflow.model.SpuUpdateResult;
import com.newzkl.platform.plugin.audit.worktable.constant.WorktableConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * SKU 基础信息工单处理器
 *
 * <p>只支持修改, 先存旧快照供操作日志, 补销售价后回写并铺货, 唯一写操作日志的处理器</p>
 *
 * @author KC
 */
@Slf4j
@Service("auditPluginSkuBaseProcessor")
public class SkuBaseProcessor extends WorktableProcessorSupport {

    @Override
    public Integer support() {
        return WorktableConst.TARGET_SKU_BASE;
    }

    @Override
    public void add(String spuUserEditInfoJson, String spuAdminEditInfoJson) {
        throw new PlatformException(BaseErrorCode.NOT_SERVICE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String spuUserEditInfoJson, String spuAdminEditInfoJson) {
        JSONObject cmd = JSON.parseObject(spuUserEditInfoJson);
        Long spuId = cmd.getLong("spuId");
        String adminUserName = cmd.getString("adminUserName");
        String oldSpuJson = spuReadPort.spuInfoJson(spuId);
        JSONArray skuArray = cmd.getJSONArray("skuVOList");
        applySalePrice(skuArray, spuAdminEditInfoJson);
        JSONObject spuDTO = new JSONObject();
        spuDTO.put("id", spuId);
        spuDTO.put("skuList", skuArray);
        String spuDtoJson = spuDTO.toJSONString();
        SpuUpdateResult result = spuWritePort.spuUpdate(spuDtoJson);
        distributionPort.upDownEvent(WorktableConst.STATE_PLATFORM_DOWN,
                Collections.singletonList(spuId), WorktableConst.YES);
        executeLogPort.save(WorktableConst.EXECUTE_TYPE_SPU_SALE_PRICE, spuId,
                adminUserName, oldSpuJson, spuDtoJson);
        notifyChannel(spuId, WorktableConst.NOTIFY_UPDATE_SKU,
                buildSkuEvent(spuId, result.updateSkuIdList()));
    }

    @Override
    public void delete(String spuUserEditInfoJson, String spuAdminEditInfoJson) {
        throw new PlatformException(BaseErrorCode.NOT_SERVICE);
    }

    @Override
    public void check(String checkCommandJson) {
        // 无需校验
    }

    /**
     * 构建 SKU 编辑事件 JSON
     *
     * @param spuId SPU 主键
     * @param skuIdList 变更的 SKU 主键列表
     * @return 事件 JSON, 结构同 ApiSkuEditEvent
     */
    private String buildSkuEvent(Long spuId, Object skuIdList) {
        JSONObject event = new JSONObject();
        event.put("spuId", spuId);
        event.put("skuIdList", skuIdList);
        return event.toJSONString();
    }
}
