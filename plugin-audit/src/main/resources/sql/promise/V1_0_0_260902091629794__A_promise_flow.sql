ALTER TABLE
  `promise_flow` DROP COLUMN `foreign_id`,
  DROP COLUMN `flow_id`,
  DROP INDEX `auto_idx_promise_flow_flow_id`,
  DROP INDEX `auto_idx_promise_flow_foreign_id`;
