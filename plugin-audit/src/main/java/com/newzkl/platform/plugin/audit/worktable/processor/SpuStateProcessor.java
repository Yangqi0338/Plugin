package com.newzkl.platform.plugin.audit.worktable.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.plugin.audit.worktable.constant.WorktableConst;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SPU 状态工单处理器
 *
 * <p>只支持修改, 按目标状态批量上下架, 再逐 SPU 通知渠道</p>
 *
 * @author KC
 */
@Service("auditPluginSpuStateProcessor")
public class SpuStateProcessor extends WorktableProcessorSupport {

    @Override
    public Integer support() {
        return WorktableConst.TARGET_SPU_STATE;
    }

    @Override
    public void add(String spuUserEditInfoJson, String spuAdminEditInfoJson) {
        throw new PlatformException(BaseErrorCode.NOT_SERVICE);
    }

    @Override
    public void update(String spuUserEditInfoJson, String spuAdminEditInfoJson) {
        JSONObject cmd = JSON.parseObject(spuUserEditInfoJson);
        Integer state = cmd.getInteger("state");
        List<Long> spuIdList = cmd.getJSONArray("spuId").toList(Long.class);
        int enable = WorktableConst.STATE_SALE == state
                ? WorktableConst.SWITCH_ON : WorktableConst.SWITCH_OFF;
        spuWritePort.spuUp(enable, spuIdList);
        for (Long spuId : spuIdList) {
            notifyChannel(spuId, WorktableConst.NOTIFY_UPDATE_SPU_STATE, buildSaleStateEvent(spuId, state));
        }
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
     * 构建商品上下架事件 JSON
     *
     * @param spuId SPU 主键
     * @param state 目标状态
     * @return 事件 JSON, 结构同 ApiGoodsSaleStateEvent
     */
    private String buildSaleStateEvent(Long spuId, Integer state) {
        JSONObject event = new JSONObject();
        event.put("spuIdList", List.of(spuId));
        event.put("skuIdList", null);
        event.put("state", state);
        return event.toJSONString();
    }
}
