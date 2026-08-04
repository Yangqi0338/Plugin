package com.newzkl.platform.plugin.openapi.action.controller;

import com.zkl.scm.developer.annotation.OpenApi;
import com.zkl.scm.developer.constants.Constants;
import com.zkl.scm.developer.utils.DeveloperContextUtil;
import com.zkl.scm.model.constants.common.YytEnum;
import com.zkl.scm.model.exception.BaseErrorCode;
import com.zkl.scm.model.exception.ThrowsException;
import com.zkl.scm.model.web.PlatformResult;
import com.zkl.scm.openapi.application.service.IRefundService;
import com.zkl.scm.openapi.interfaces.controller.RefundCmd;
import com.zkl.scm.rpc.model.ApiPage;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 开放平台-售后
 * @author fang
 */
@RestController("开放平台-售后")
@RequestMapping("/api/refund")
@Setter(onMethod_ = @Autowired)
@Validated
public class RefundController {

    private final IRefundService refundService;

    public RefundController(IRefundService refundService) {
        this.refundService = refundService;
    }

    /**
     * 申请售后
     * @return 售后单号
     */
    @PostMapping("submit")
    public PlatformResult<Long> submit(@Validated @RequestBody ApiRefundSubmitReq refundSubmitReq) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        return PlatformResult.success(refundService.submit(accountId, refundSubmitReq));
    }
    /**
     * 取消售后
     */
    @PostMapping("/stop")
    public PlatformResult<Void> stop(@RequestBody RefundCmd.RefundId refundId) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        refundService.stop(accountId, refundId.getRefundId());
        return PlatformResult.success();
    }
    /**
     * 提交退货物流
     */
    @PostMapping("/submitFreight")
    public PlatformResult<Void> submitFreight(@RequestBody ApiRefundFreightReq refundFreightReq) {
        YytEnum.ExpressType expressType = YytEnum.ExpressType.getByValue(refundFreightReq.getFreightCompanyName().trim());
        if(expressType == null){
            expressType = YytEnum.ExpressType.getByCodeLike(refundFreightReq.getFreightCompanyName());
            if(expressType == null){
                ThrowsException.exception(BaseErrorCode.PARAM, "请填写指定的物流公司名称简称, 如 顺丰、韵达、申通、中通、汇通、圆通、极兔、邮政");
            }
        }
        refundFreightReq.setFreightCompanyName(expressType.getCode());
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        refundService.submitFreight(accountId, refundFreightReq);
        return PlatformResult.success();
    }
    /**
     * 获取退货地址
     */
    @PostMapping("/freightAddress")
    public PlatformResult<ApiRefundFreightAddressVO> freightAddress(@RequestBody ApiFreightAddressReq refundAddressInfoReq){
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        return PlatformResult.success(refundService.freightAddress(accountId, refundAddressInfoReq));
    }
    /**
     * 查询售后状态
     */
    @PostMapping("/refundState")
    public PlatformResult<List<ApiRefundStateVO>> refundState(@RequestBody RefundCmd.RefundIdList refundIdList) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        return PlatformResult.success(refundService.refundState(accountId, refundIdList.getRefundIdList()));
    }
    /**
     * 查询售后列表
     */
    @PostMapping("list")
    public PlatformResult<ApiPage<ApiRefundVO>> list(@Validated @RequestBody ApiRefundReq apiRefundReq) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        ApiPage<ApiRefundVO> page = refundService.list(accountId, apiRefundReq);
        return PlatformResult.success(page);
    }
    /**
     * 查询售后详情
     */
    @PostMapping("detail")
    public PlatformResult<ApiRefundAggVO> detail(@Validated @RequestBody RefundCmd.RefundId refundId) {
        Long accountId = DeveloperContextUtil.get(Constants.ACCOUNT_ID, Long.class);
        ApiRefundAggVO detailVO = refundService.detail(accountId, refundId.getRefundId());
        return PlatformResult.success(detailVO);
    }
}
