package com.newzkl.platform.plugin.audit.worktable.processor;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.plugin.audit.port.ChannelRelationPort;
import com.newzkl.platform.plugin.audit.port.DistributionPort;
import com.newzkl.platform.plugin.audit.port.ExecuteLogPort;
import com.newzkl.platform.plugin.audit.port.OpenapiNotifyPort;
import com.newzkl.platform.plugin.audit.port.SpuReadPort;
import com.newzkl.platform.plugin.audit.port.SpuWritePort;
import com.newzkl.platform.plugin.audit.port.WorktableDataPort;
import com.newzkl.platform.plugin.audit.worktable.constant.WorktableConst;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 工单执行处理器基类
 *
 * <p>按 support() 返回的 operateTarget 分派, 子类必须标注 @Service 才会被 WorktableFactory 注册,
 * 基类自身不注册为 bean, 这是原 NPE 真因的修复点</p>
 *
 * @author KC
 */
public abstract class WorktableProcessorSupport {

    @Autowired
    protected SpuReadPort spuReadPort;
    @Autowired
    protected WorktableDataPort worktableDataPort;
    @Autowired
    protected ChannelRelationPort channelRelationPort;
    @Autowired
    protected OpenapiNotifyPort openapiNotifyPort;
    @Autowired
    protected SpuWritePort spuWritePort;
    @Autowired
    protected DistributionPort distributionPort;
    @Autowired
    protected ExecuteLogPort executeLogPort;

    /**
     * 本处理器支持的操作目标
     *
     * @return operateTarget 1~5
     */
    public abstract Integer support();

    /**
     * 审批通过后执行新增
     *
     * @param spuUserEditInfoJson 用户提交的变更 JSON
     * @param spuAdminEditInfoJson 审核录入的补充 JSON
     */
    public abstract void add(String spuUserEditInfoJson, String spuAdminEditInfoJson);

    /**
     * 审批通过后执行修改
     *
     * @param spuUserEditInfoJson 用户提交的变更 JSON
     * @param spuAdminEditInfoJson 审核录入的补充 JSON
     */
    public abstract void update(String spuUserEditInfoJson, String spuAdminEditInfoJson);

    /**
     * 审批通过后执行删除
     *
     * @param spuUserEditInfoJson 用户提交的变更 JSON
     * @param spuAdminEditInfoJson 审核录入的补充 JSON
     */
    public abstract void delete(String spuUserEditInfoJson, String spuAdminEditInfoJson);

    /**
     * 提交前校验
     *
     * @param checkCommandJson 校验命令 JSON
     */
    public abstract void check(String checkCommandJson);

    /**
     * 按 tempId 回填审核时确定的销售价
     *
     * <p>源码在 SaleAttribute 与 SkuBase 两处复制, 此处上提消重, 行为不变</p>
     *
     * @param skuArray 待回填的 SKU JSON 数组, 就地改
     * @param spuAdminEditInfoJson 审核录入的价格 JSON, 结构 {tempId: salePrice}
     */
    protected void applySalePrice(JSONArray skuArray, String spuAdminEditInfoJson) {
        JSONObject priceMap = JSONObject.parseObject(spuAdminEditInfoJson);
        for (int i = 0; i < skuArray.size(); i++) {
            JSONObject sku = skuArray.getJSONObject(i);
            Object tempId = sku.get("tempId");
            if (tempId == null) {
                continue;
            }
            Object price = priceMap.get(tempId.toString());
            if (price != null) {
                sku.put("salePrice", Integer.valueOf(String.valueOf(price)));
            }
        }
    }

    /**
     * 逐渠道发送商品变更通知
     *
     * <p>旁路动作, 缺渠道关系或通知基建时端口降级, 不阻断主流程</p>
     *
     * @param spuId SPU 主键
     * @param businessType 通知业务类型
     * @param eventInfoJson 事件内容 JSON
     */
    protected void notifyChannel(Long spuId, Integer businessType, String eventInfoJson) {
        List<Long> channelIdList = channelRelationPort.spuChannelRelation(spuId);
        openapiNotifyPort.batchSend(channelIdList, WorktableConst.NOTIFY_SERVICE_GOODS, businessType, eventInfoJson);
    }
}
