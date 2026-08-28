ALTER TABLE
  `promise_flow` MODIFY COLUMN `account_id` bigint NULL COMMENT 'accountId',
  MODIFY COLUMN `identity` int NULL COMMENT 'identity[1平台管理员,2平台员工,1000会员,1001供应商,1002渠道商,1003服务商,1004脉脉通渠道商]',
  MODIFY COLUMN `promise_pay_type` int NULL COMMENT 'promisePayType',
  MODIFY COLUMN `amount` bigint NULL COMMENT 'amount',
  MODIFY COLUMN `pay_type` int NULL COMMENT 'payType[0直接,1微信,2支付宝,3采购金,4兑换码]',
  MODIFY COLUMN `certificate_url` varchar(255) NULL COMMENT 'certificateUrl',
  MODIFY COLUMN `audit_state` int NULL COMMENT 'auditState[0待用户提交,1待审核,2通过,3未通过,4终止]',
  MODIFY COLUMN `audit_refuse_reason` varchar(255) NULL COMMENT 'auditRefuseReason',
ADD
  COLUMN `foreign_id` bigint NULL COMMENT '外键id' AFTER `audit_refuse_reason`,
ADD
  COLUMN `flow_id` bigint NULL COMMENT '审批流id' AFTER `foreign_id`,
ADD
  INDEX `auto_idx_promise_flow_foreign_id`(`foreign_id`) COMMENT '外键id',
ADD
  INDEX `auto_idx_promise_flow_flow_id`(`flow_id`) COMMENT '审批流id',
  COMMENT = 'PromiseFlowDO表';
