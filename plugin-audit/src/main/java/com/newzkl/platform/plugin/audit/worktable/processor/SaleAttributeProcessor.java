package com.newzkl.platform.plugin.audit.worktable.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.plugin.audit.workflow.model.SpuUpdateResult;
import com.newzkl.platform.plugin.audit.worktable.constant.WorktableConst;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 销售属性工单处理器
 *
 * <p>支持新增与删除规格; 新增补销售价并铺货, 删除仅回写; check 校验笛卡尔积新增数</p>
 *
 * @author KC
 */
@Service("auditPluginSaleAttributeProcessor")
public class SaleAttributeProcessor extends WorktableProcessorSupport {

    @Override
    public Integer support() {
        return WorktableConst.TARGET_SALE_ATTRIBUTE;
    }

    @Override
    public void add(String spuUserEditInfoJson, String spuAdminEditInfoJson) {
        JSONObject addCommand = JSON.parseObject(spuUserEditInfoJson);
        Long spuId = addCommand.getLong("spuId");
        JSONArray skuList = addCommand.getJSONArray("skuList");
        applySalePrice(skuList, spuAdminEditInfoJson);
        JSONObject spuDTO = new JSONObject();
        spuDTO.put("id", spuId);
        spuDTO.put("skuList", skuList);
        spuDTO.put("spuSaleAttributeList", addCommand.get("spuSaleAttributeList"));
        SpuUpdateResult result = spuWritePort.spuUpdate(spuDTO.toJSONString());
        distributionPort.upDownEvent(WorktableConst.STATE_PLATFORM_DOWN,
                Collections.singletonList(spuId), WorktableConst.YES);
        notifyChannel(spuId, WorktableConst.NOTIFY_UPDATE_SKU,
                buildSkuEvent(spuId, result.addSkuIdList()));
    }

    @Override
    public void update(String spuUserEditInfoJson, String spuAdminEditInfoJson) {
        throw new PlatformException(BaseErrorCode.NOT_SERVICE);
    }

    @Override
    public void delete(String spuUserEditInfoJson, String spuAdminEditInfoJson) {
        JSONObject deleteCommand = JSON.parseObject(spuUserEditInfoJson);
        Long spuId = deleteCommand.getLong("spuId");
        JSONObject spuDTO = new JSONObject();
        spuDTO.put("id", spuId);
        spuDTO.put("spuSaleAttributeList", deleteCommand.get("spuSaleAttributeList"));
        SpuUpdateResult result = spuWritePort.spuUpdate(spuDTO.toJSONString());
        notifyChannel(spuId, WorktableConst.NOTIFY_DELETE_SKU,
                buildSkuEvent(spuId, result.deleteSkuIdList()));
    }

    @Override
    public void check(String checkCommandJson) {
        JSONObject checkCommand = JSON.parseObject(checkCommandJson);
        JSONArray oldAttrs = checkCommand.getJSONObject("oldSpu").getJSONArray("spuSaleAttributeList");
        JSONArray newAttrs = checkCommand.getJSONArray("attributeVOList");
        int needAddSkuNum = getNeedAddSkuNum(oldAttrs, newAttrs);
        int currentAddSkuNum = getCurrentAddSkuNum(checkCommand.getJSONArray("skuList"));
        if (needAddSkuNum != currentAddSkuNum) {
            throw new PlatformException(BaseErrorCode.PARAM, "新增SKU数量不对,应为:" + needAddSkuNum);
        }
    }

    /**
     * 计算应新增的 SKU 数量
     *
     * <p>新旧销售属性各自取值个数连乘得笛卡尔积, 差值即应新增数</p>
     *
     * @param oldAttrs 旧销售属性数组, 每项 value 为取值 JSON 数组字符串
     * @param newAttrs 新销售属性数组
     * @return 应新增 SKU 数量
     */
    private int getNeedAddSkuNum(JSONArray oldAttrs, JSONArray newAttrs) {
        int newSkuNum = 1;
        int oldSkuNum = 1;
        for (int i = 0; i < newAttrs.size(); i++) {
            newSkuNum = newSkuNum * JSON.parseArray(newAttrs.getJSONObject(i).getString("value")).size();
        }
        for (int i = 0; i < oldAttrs.size(); i++) {
            oldSkuNum = oldSkuNum * JSON.parseArray(oldAttrs.getJSONObject(i).getString("value")).size();
        }
        return newSkuNum - oldSkuNum;
    }

    /**
     * 统计当前待新增的 SKU 数量
     *
     * @param skuList SKU 数组
     * @return id 为空的 SKU 条数
     */
    private int getCurrentAddSkuNum(JSONArray skuList) {
        int addSkuNum = 0;
        for (int i = 0; i < skuList.size(); i++) {
            if (skuList.getJSONObject(i).get("id") == null) {
                addSkuNum++;
            }
        }
        return addSkuNum;
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
