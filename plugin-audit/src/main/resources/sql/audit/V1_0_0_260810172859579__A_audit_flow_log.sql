ALTER TABLE
  `audit_flow_log` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `flow_id` bigint NULL COMMENT 'flowId',
  MODIFY COLUMN `account_id` bigint NULL COMMENT 'accountId',
  MODIFY COLUMN `account_name` varchar(255) NULL COMMENT 'accountName',
  MODIFY COLUMN `audit_operate` int NULL COMMENT 'auditOperate',
ADD
  COLUMN `foreign_id` bigint NULL COMMENT '外键id' AFTER `audit_operate`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `foreign_id`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_audit_flow_log_foreign_id`(`foreign_id`) COMMENT '外键id',
  COMMENT = 'AuditFlowLogDO表';
