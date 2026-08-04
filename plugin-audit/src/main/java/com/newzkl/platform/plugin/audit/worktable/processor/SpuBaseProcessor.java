package com.newzkl.platform.plugin.audit.worktable.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.plugin.audit.worktable.constant.WorktableConst;
import org.springframework.stereotype.Service;

/**
 * SPU 基础信息工单处理器
 *
 * <p>只支持修改, 置空 sku/规格/状态/账号字段, 确保仅改主体信息</p>
 *
 * @author KC
 */
@Service("auditPluginSpuBaseProcessor")
public class SpuBaseProcessor extends WorktableProcessorSupport {

    @Override
    public Integer support() {
        return WorktableConst.TARGET_SPU_BASE;
    }

    @Override
    public void add(String spuUserEditInfoJson, String spuAdminEditInfoJson) {
        throw new PlatformException(BaseErrorCode.NOT_SERVICE);
    }

    @Override
    public void update(String spuUserEditInfoJson, String spuAdminEditInfoJson) {
        JSONObject cmd = JSON.parseObject(spuUserEditInfoJson);
        JSONObject spuDTO = cmd.getJSONObject("spuDTO");
        Long spuId = spuDTO.getLong("id");
        spuDTO.put("skuList", null);
        spuDTO.put("spuSaleAttributeList", null);
        spuDTO.put("state", null);
        spuDTO.put("accountId", null);
        spuWritePort.spuUpdate(spuDTO.toJSONString());
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
