ALTER TABLE
  `audit_template` DROP COLUMN `template_nodes_json`,
  MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
ADD
  COLUMN `template_type` int NULL COMMENT 'templateType' AFTER `id`,
  MODIFY COLUMN `name` varchar(255) NULL COMMENT 'name',
  MODIFY COLUMN `node` json NULL COMMENT 'node' AFTER `name`,
  MODIFY COLUMN `edge` json NULL COMMENT 'edge' AFTER `node`,
ADD
  COLUMN `foreign_id` bigint NULL COMMENT '外键id' AFTER `edge`,
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
  INDEX `auto_idx_audit_template_foreign_id`(`foreign_id`) COMMENT '外键id',
ADD
  INDEX `auto_idx_audit_template_flow_id`(`flow_id`) COMMENT '审批流id',
  COMMENT = 'AuditTemplateDO表';
