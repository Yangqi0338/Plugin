package com.newzkl.platform.plugin.audit.workflow.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.plugin.audit.port.WorktableDataPort;
import com.newzkl.platform.plugin.audit.worktable.constant.WorktableConst;
import com.newzkl.platform.plugin.audit.worktable.processor.WorktableFactory;
import com.newzkl.platform.plugin.audit.workflow.constant.AuditEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工单审批回调服务实现
 *
 * <p>通过则先落审批人改动, 再按 operateType 分派处理器执行 add/update/delete</p>
 *
 * @author KC
 */
@Slf4j
@Service("auditPluginWorktableApprovalService")
@RequiredArgsConstructor
public class WorktableApprovalServiceImpl implements WorktableApprovalService {

    private final WorktableDataPort worktableDataPort;
    private final WorktableFactory worktableFactory;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approval(Long workTableId, String state, String editCommand) {
        if (!AuditEnum.State.SUCCESS.name().equals(state)) {
            return;
        }
        String detailJson = worktableDataPort.detail(workTableId);
        if (StringUtils.isEmpty(detailJson)) {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
        if (StringUtils.isNotEmpty(editCommand)) {
            worktableDataPort.editSkuSalePrice(workTableId, editCommand);
        }
        JSONObject data = JSON.parseObject(detailJson);
        Integer operateType = data.getInteger("operateType");
        Integer operateTarget = data.getInteger("operateTarget");
        String spuEditInfoJson = data.getString("spuEditInfoJson");
        String skuSalePriceJson = data.getString("skuSalePriceJson");
        if (Integer.valueOf(WorktableConst.TYPE_ADD).equals(operateType)) {
            worktableFactory.getPolicy(operateTarget).add(spuEditInfoJson, skuSalePriceJson);
        } else if (Integer.valueOf(WorktableConst.TYPE_UPDATE).equals(operateType)) {
            worktableFactory.getPolicy(operateTarget).update(spuEditInfoJson, skuSalePriceJson);
        } else if (Integer.valueOf(WorktableConst.TYPE_DELETE).equals(operateType)) {
            worktableFactory.getPolicy(operateTarget).delete(spuEditInfoJson, skuSalePriceJson);
        } else {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
    }
}
