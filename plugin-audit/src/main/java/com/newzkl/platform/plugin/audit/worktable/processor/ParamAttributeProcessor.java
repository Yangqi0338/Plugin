package com.newzkl.platform.plugin.audit.worktable.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.plugin.audit.worktable.constant.WorktableConst;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * 参数属性工单处理器
 *
 * <p>只支持修改, 改 SPU 参数属性后铺货并通知; 无提交入口, 由属性编辑间接创建工单</p>
 *
 * @author KC
 */
@Service("auditPluginParamAttributeProcessor")
public class ParamAttributeProcessor extends WorktableProcessorSupport {

    @Override
    public Integer support() {
        return WorktableConst.TARGET_PARAM_ATTRIBUTE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(String spuUserEditInfoJson, String spuAdminEditInfoJson) {
        throw new PlatformException(BaseErrorCode.NOT_SERVICE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String spuUserEditInfoJson, String spuAdminEditInfoJson) {
        JSONObject cmd = JSON.parseObject(spuUserEditInfoJson);
        Long spuId = cmd.getLong("spuId");
        JSONObject spuDTO = new JSONObject();
        spuDTO.put("id", spuId);
        spuDTO.put("spuParamAttributeList", cmd.get("attributeVOList"));
        spuWritePort.spuUpdate(spuDTO.toJSONString());
        distributionPort.upDownEvent(WorktableConst.STATE_PLATFORM_DOWN,
                Collections.singletonList(spuId), WorktableConst.YES);
        notifyChannel(spuId, WorktableConst.NOTIFY_UPDATE_SPU, buildSpuEvent(spuId));
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
     * 构建 SPU 编辑事件 JSON
     *
     * @param spuId SPU 主键
     * @return 事件 JSON, 结构同 ApiSpuEditEvent
     */
    private String buildSpuEvent(Long spuId) {
        JSONObject event = new JSONObject();
        event.put("spuId", spuId);
        return event.toJSONString();
    }
}
