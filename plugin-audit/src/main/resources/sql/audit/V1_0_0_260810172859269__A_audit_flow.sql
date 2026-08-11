ALTER TABLE
  `audit_flow` DROP COLUMN `current_audit_account_id`,
  DROP COLUMN `current_audit_role_id`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `template_id` bigint NULL COMMENT 'templateId',
  MODIFY COLUMN `account_id` bigint NULL COMMENT 'accountId',
  MODIFY COLUMN `username` varchar(255) NULL COMMENT 'username' AFTER `account_id`,
  MODIFY COLUMN `role_id` bigint NULL COMMENT 'roleId' AFTER `username`,
  MODIFY COLUMN `state` int NULL COMMENT 'state',
  MODIFY COLUMN `current_code` varchar(255) NULL COMMENT 'currentCode',
  MODIFY COLUMN `context_params` varchar(255) NULL COMMENT 'contextParams' AFTER `current_code`,
  MODIFY COLUMN `last_refuse_reason` varchar(255) NULL COMMENT 'lastRefuseReason',
  MODIFY COLUMN `is_new` int NULL COMMENT 'isNew' AFTER `last_refuse_reason`,
ADD
  COLUMN `foreign_id` bigint NULL COMMENT '外键id' AFTER `is_new`,
ADD
  COLUMN `flow_id` bigint NULL COMMENT '审批流id' AFTER `foreign_id`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `flow_id`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_audit_flow_foreign_id`(`foreign_id`) COMMENT '外键id',
ADD
  INDEX `auto_idx_audit_flow_flow_id`(`flow_id`) COMMENT '审批流id',
  COMMENT = 'AuditFlowDO表';
