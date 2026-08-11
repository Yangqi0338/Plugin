ALTER TABLE
  `developer` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键ID',
  MODIFY COLUMN `app_id` varchar(255) NULL COMMENT 'appId' AFTER `id`,
  MODIFY COLUMN `app_name` varchar(255) NULL COMMENT 'appName',
  MODIFY COLUMN `secret` varchar(255) NULL COMMENT 'secret',
  MODIFY COLUMN `account_id` bigint NULL COMMENT 'accountId',
  MODIFY COLUMN `remark` varchar(255) NULL COMMENT 'remark',
  MODIFY COLUMN `notify_address` varchar(255) NULL COMMENT 'notifyAddress' AFTER `remark`,
ADD
  COLUMN `executor` json NULL COMMENT '操作人信息' AFTER `notify_address`,
ADD
  COLUMN `creator_id` bigint NULL COMMENT '创建人id' AFTER `executor`,
  MODIFY COLUMN `create_time` datetime NULL COMMENT '创建时间',
  MODIFY COLUMN `update_time` datetime NULL COMMENT '更新时间',
ADD
  COLUMN `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标记(正常 0, 删除为 NULL(确保唯一索引生效))',
ADD
  INDEX `auto_idx_developer_app_id`(`app_id`) COMMENT 'appId',
ADD
  INDEX `auto_idx_developer_account_id`(`account_id`) COMMENT 'accountId',
  COMMENT = 'DeveloperDO表';
