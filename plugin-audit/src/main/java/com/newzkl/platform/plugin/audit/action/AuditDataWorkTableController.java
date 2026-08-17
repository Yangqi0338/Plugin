package com.newzkl.platform.plugin.audit.action;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.req.IdCommand;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.audit.port.WorktableDataPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台-工单审批数据控制器
 *
 * <p>工单审批数据增删改查, URL 前缀沿用源 /xxx/auditDataWorkTable 不改</p>
 *
 * @author KC
 */
@RestController("auditPluginDataWorkTableController")
@RequestMapping("/xxx/auditDataWorkTable")
@RequiredArgsConstructor
public class AuditDataWorkTableController {

    private final WorktableDataPort worktableDataPort;

    /**
     * 创建工单审批数据
     *
     * @param dataVoJson 工单审批数据 JSON, id 必须为空
     * @return 新建审批数据主键
     */
    @PostMapping("auditDataWorkTableCreate")
    public PlatformResult<Long> auditDataWorkTableCreate(@RequestBody String dataVoJson) {
        JSONObject obj = JSON.parseObject(dataVoJson);
        if (obj.get("id") != null) {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
        return PlatformResult.success(worktableDataPort.save(dataVoJson));
    }

    /**
     * 删除工单审批数据
     *
     * @param idListObj ID 列表入参
     * @return 操作结果
     */
    @PostMapping("auditDataWorkTableDelete")
    public PlatformResult<Void> auditDataWorkTableDelete(@RequestBody IdCommand idListObj) {
        worktableDataPort.delete(idListObj.getIdList());
        return PlatformResult.success();
    }

    /**
     * 修改工单审批数据
     *
     * @param dataVoJson 工单审批数据 JSON, id 不能为空
     * @return 操作结果
     */
    @PostMapping("auditDataWorkTableUpdate")
    public PlatformResult<Void> auditDataWorkTableUpdate(@RequestBody String dataVoJson) {
        JSONObject obj = JSON.parseObject(dataVoJson);
        if (obj.get("id") == null) {
            throw new PlatformException(BaseErrorCode.PARAM);
        }
        worktableDataPort.save(dataVoJson);
        return PlatformResult.success();
    }

    /**
     * 工单审批数据详情
     *
     * @param id 审批数据主键
     * @return 工单审批数据 JSON
     */
    @GetMapping("auditDataWorkTable")
    public PlatformResult<String> auditDataWorkTable(@RequestParam("id") Long id) {
        return PlatformResult.success(worktableDataPort.detail(id));
    }

    /**
     * 工单审批数据分页
     *
     * @param queryJson 查询条件 JSON
     * @return 分页结果 JSON
     */
    @PostMapping("auditDataWorkTablePage")
    public PlatformResult<String> auditDataWorkTablePageVOList(@RequestBody String queryJson) {
        return PlatformResult.success(worktableDataPort.pageJson(queryJson));
    }
}
