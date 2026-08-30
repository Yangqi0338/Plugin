package com.newzkl.platform.plugin.audit.action.cmd;

import com.newzkl.platform.base.biz.account.model.vo.CompanyInfoVO;

/**
 * 供应商审核提交命令
 *
 * <p>供应商注册后补充企业资质资料提交审核的入参, companyInfo 为结构化对象, 透出前端字段契约</p>
 *
 * @param companyInfo 企业资质信息
 * @author KC
 */
public record SupplierAuditSubmitCommand(CompanyInfoVO companyInfo) {
}
