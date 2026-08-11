package com.newzkl.platform.plugin.audit.action;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.audit.port.SpuReadPort;
import com.newzkl.platform.plugin.audit.worktable.constant.WorktableConst;
import com.newzkl.platform.plugin.audit.worktable.processor.WorktableFactory;
import com.newzkl.platform.plugin.audit.workflow.service.WorktableSubmitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 商品-工单控制器
 *
 * <p>商品变更走工单审批: 提交后落审批数据, 由平台审核后再生效</p>
 *
 * @author KC
 */
@RestController("auditPluginWorktableController")
@RequestMapping("/goods/worktable")
@RequiredArgsConstructor
public class WorktableController {

    private final WorktableSubmitService worktableSubmitService;
    private final WorktableFactory worktableFactory;
    private final SpuReadPort spuReadPort;

    /**
     * 提交 SPU 基础信息修改工单
     *
     * @param spuUpdateJson SPU 基础信息修改命令 JSON, 含 spuDTO.id
     * @return 空结果
     */
    // TODO[auth-defer]: 源 @Limit(worktable, set) 待入口 starter 鉴权切面接入
    @PostMapping("spuUpdate")
    public PlatformResult<Void> spuUpdate(@RequestBody String spuUpdateJson) {
        JSONObject cmd = JSON.parseObject(spuUpdateJson);
        Long spuId = cmd.getJSONObject("spuDTO").getLong("id");
        worktableSubmitService.submitWorkTable(Collections.singletonList(spuId),
                WorktableConst.TARGET_SPU_BASE, WorktableConst.TYPE_UPDATE, spuUpdateJson);
        return PlatformResult.success();
    }

    /**
     * 提交 SKU 基础信息修改工单
     *
     * @param skuUpdateJson SKU 基础信息修改命令 JSON, 含 spuId 与 skuVOList
     * @return 空结果
     */
    // TODO[auth-defer]: 源 @Limit(worktable, set)
    @PostMapping("skuUpdate")
    public PlatformResult<Void> skuUpdate(@RequestBody String skuUpdateJson) {
        JSONObject cmd = JSON.parseObject(skuUpdateJson);
        Long spuId = cmd.getLong("spuId");
        stampTempId(cmd.getJSONArray("skuVOList"));
        worktableSubmitService.submitWorkTable(Collections.singletonList(spuId),
                WorktableConst.TARGET_SKU_BASE, WorktableConst.TYPE_UPDATE, cmd.toJSONString());
        return PlatformResult.success();
    }

    /**
     * 提交 SPU 状态修改工单
     *
     * @param spuStateJson SPU 状态修改命令 JSON, 含 spuId 列表与 state
     * @return 空结果
     */
    // TODO[auth-defer]: 源 @Limit(worktable, set)
    @PostMapping("spuStateUpdate")
    public PlatformResult<Void> spuStateUpdate(@RequestBody String spuStateJson) {
        JSONObject cmd = JSON.parseObject(spuStateJson);
        List<Long> spuIdList = cmd.getJSONArray("spuId").toList(Long.class);
        worktableSubmitService.submitWorkTable(spuIdList,
                WorktableConst.TARGET_SPU_STATE, WorktableConst.TYPE_UPDATE, spuStateJson);
        return PlatformResult.success();
    }

    /**
     * 提交规格删除工单
     *
     * @param saleAttributeJson 规格删除命令 JSON, 含 spuId
     * @return 空结果
     */
    // TODO[auth-defer]: 源 @Limit(worktable, set)
    @PostMapping("saleAttributeDelete")
    public PlatformResult<Void> saleAttributeDelete(@RequestBody String saleAttributeJson) {
        JSONObject cmd = JSON.parseObject(saleAttributeJson);
        Long spuId = cmd.getLong("spuId");
        worktableSubmitService.submitWorkTable(Collections.singletonList(spuId),
                WorktableConst.TARGET_SALE_ATTRIBUTE, WorktableConst.TYPE_DELETE, saleAttributeJson);
        return PlatformResult.success();
    }

    /**
     * 提交规格新增工单
     *
     * <p>提交前先做规格数据校验, 校验通过再落工单</p>
     *
     * @param saleAttributeJson 规格新增命令 JSON, 含 spuId 与 skuList 与 attributeVOList
     * @return 空结果
     */
    // 源 saleAttributeAdd 无 @Limit, 照抄这个不对称
    @PostMapping("saleAttributeAdd")
    public PlatformResult<Void> saleAttributeAdd(@RequestBody String saleAttributeJson) {
        JSONObject cmd = JSON.parseObject(saleAttributeJson);
        Long spuId = cmd.getLong("spuId");
        JSONArray skuList = cmd.getJSONArray("skuList");
        stampTempId(skuList);
        JSONObject check = new JSONObject();
        check.put("spuId", spuId);
        check.put("oldSpu", JSON.parseObject(spuReadPort.spuInfoJson(spuId)));
        check.put("skuList", skuList);
        check.put("attributeVOList", cmd.getJSONArray("attributeVOList"));
        worktableFactory.getPolicy(WorktableConst.TARGET_SALE_ATTRIBUTE).check(check.toJSONString());
        worktableSubmitService.submitWorkTable(Collections.singletonList(spuId),
                WorktableConst.TARGET_SALE_ATTRIBUTE, WorktableConst.TYPE_ADD, cmd.toJSONString());
        return PlatformResult.success();
    }

    /**
     * 为待新增 SKU 逐个补雪花 tempId
     *
     * @param skuArray SKU JSON 数组, 就地改
     */
    private void stampTempId(JSONArray skuArray) {
        if (skuArray == null) {
            return;
        }
        for (int i = 0; i < skuArray.size(); i++) {
            skuArray.getJSONObject(i).put("tempId", SnowflakeGenerator.getSnowflakeId());
        }
    }
}
