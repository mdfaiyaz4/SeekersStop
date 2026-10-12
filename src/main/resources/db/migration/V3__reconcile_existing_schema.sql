-- Apply the approved disposition only when job 1 is not already active.
-- This data correction must precede the active NOT NULL constraint below.
UPDATE `job`
SET `active` = b'1'
WHERE `id` = 1
  AND (`active` IS NULL OR `active` <> b'1');

-- Tighten nullable columns only where the existing schema still allows NULL.
SET @migration_sql = IF(
    (SELECT IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'username') = 'YES',
    'ALTER TABLE `user` MODIFY COLUMN `username` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL',
    'SELECT 1'
);
PREPARE migration_stmt FROM @migration_sql;
EXECUTE migration_stmt;
DEALLOCATE PREPARE migration_stmt;

SET @migration_sql = IF(
    (SELECT IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'password') = 'YES',
    'ALTER TABLE `user` MODIFY COLUMN `password` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL',
    'SELECT 1'
);
PREPARE migration_stmt FROM @migration_sql;
EXECUTE migration_stmt;
DEALLOCATE PREPARE migration_stmt;

SET @migration_sql = IF(
    (SELECT IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'role') = 'YES',
    'ALTER TABLE `user` MODIFY COLUMN `role` ENUM(''JOB_SEEKER'',''RECRUITER'') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL',
    'SELECT 1'
);
PREPARE migration_stmt FROM @migration_sql;
EXECUTE migration_stmt;
DEALLOCATE PREPARE migration_stmt;

SET @migration_sql = IF(
    (SELECT IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'job' AND COLUMN_NAME = 'active') = 'YES',
    'ALTER TABLE `job` MODIFY COLUMN `active` BIT(1) NOT NULL',
    'SELECT 1'
);
PREPARE migration_stmt FROM @migration_sql;
EXECUTE migration_stmt;
DEALLOCATE PREPARE migration_stmt;

SET @migration_sql = IF(
    (SELECT IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'job_seeker' AND COLUMN_NAME = 'user_id') = 'YES',
    'ALTER TABLE `job_seeker` MODIFY COLUMN `user_id` BIGINT NOT NULL',
    'SELECT 1'
);
PREPARE migration_stmt FROM @migration_sql;
EXECUTE migration_stmt;
DEALLOCATE PREPARE migration_stmt;

SET @migration_sql = IF(
    (SELECT IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'recruiter' AND COLUMN_NAME = 'user_id') = 'YES',
    'ALTER TABLE `recruiter` MODIFY COLUMN `user_id` BIGINT NOT NULL',
    'SELECT 1'
);
PREPARE migration_stmt FROM @migration_sql;
EXECUTE migration_stmt;
DEALLOCATE PREPARE migration_stmt;

SET @migration_sql = IF(
    (SELECT IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'application' AND COLUMN_NAME = 'job_id') = 'YES',
    'ALTER TABLE `application` MODIFY COLUMN `job_id` BIGINT NOT NULL',
    'SELECT 1'
);
PREPARE migration_stmt FROM @migration_sql;
EXECUTE migration_stmt;
DEALLOCATE PREPARE migration_stmt;

SET @migration_sql = IF(
    (SELECT IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'application' AND COLUMN_NAME = 'job_seeker_id') = 'YES',
    'ALTER TABLE `application` MODIFY COLUMN `job_seeker_id` BIGINT NOT NULL',
    'SELECT 1'
);
PREPARE migration_stmt FROM @migration_sql;
EXECUTE migration_stmt;
DEALLOCATE PREPARE migration_stmt;

-- Add each unique key only if an equivalent non-prefix unique index is absent.
SELECT COUNT(*) INTO @has_user_username_unique
FROM (
    SELECT INDEX_NAME
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND NON_UNIQUE = 0
    GROUP BY INDEX_NAME
    HAVING COUNT(*) = 1
       AND GROUP_CONCAT(COLUMN_NAME ORDER BY SEQ_IN_INDEX SEPARATOR ',') = 'username'
       AND SUM(SUB_PART IS NOT NULL) = 0
) AS existing_unique;
SET @migration_sql = IF(@has_user_username_unique = 0,
    'ALTER TABLE `user` ADD CONSTRAINT `UK_user_username` UNIQUE (`username`)', 'SELECT 1');
PREPARE migration_stmt FROM @migration_sql;
EXECUTE migration_stmt;
DEALLOCATE PREPARE migration_stmt;

SELECT COUNT(*) INTO @has_job_seeker_user_unique
FROM (
    SELECT INDEX_NAME
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'job_seeker' AND NON_UNIQUE = 0
    GROUP BY INDEX_NAME
    HAVING COUNT(*) = 1
       AND GROUP_CONCAT(COLUMN_NAME ORDER BY SEQ_IN_INDEX SEPARATOR ',') = 'user_id'
       AND SUM(SUB_PART IS NOT NULL) = 0
) AS existing_unique;
SET @migration_sql = IF(@has_job_seeker_user_unique = 0,
    'ALTER TABLE `job_seeker` ADD CONSTRAINT `UK_job_seeker_user_id` UNIQUE (`user_id`)', 'SELECT 1');
PREPARE migration_stmt FROM @migration_sql;
EXECUTE migration_stmt;
DEALLOCATE PREPARE migration_stmt;

SELECT COUNT(*) INTO @has_recruiter_user_unique
FROM (
    SELECT INDEX_NAME
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'recruiter' AND NON_UNIQUE = 0
    GROUP BY INDEX_NAME
    HAVING COUNT(*) = 1
       AND GROUP_CONCAT(COLUMN_NAME ORDER BY SEQ_IN_INDEX SEPARATOR ',') = 'user_id'
       AND SUM(SUB_PART IS NOT NULL) = 0
) AS existing_unique;
SET @migration_sql = IF(@has_recruiter_user_unique = 0,
    'ALTER TABLE `recruiter` ADD CONSTRAINT `UK_recruiter_user_id` UNIQUE (`user_id`)', 'SELECT 1');
PREPARE migration_stmt FROM @migration_sql;
EXECUTE migration_stmt;
DEALLOCATE PREPARE migration_stmt;

SELECT COUNT(*) INTO @has_application_pair_unique
FROM (
    SELECT INDEX_NAME
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'application' AND NON_UNIQUE = 0
    GROUP BY INDEX_NAME
    HAVING COUNT(*) = 2
       AND GROUP_CONCAT(COLUMN_NAME ORDER BY SEQ_IN_INDEX SEPARATOR ',') = 'job_id,job_seeker_id'
       AND SUM(SUB_PART IS NOT NULL) = 0
) AS existing_unique;
SET @migration_sql = IF(@has_application_pair_unique = 0,
    'ALTER TABLE `application` ADD CONSTRAINT `UK_application_job_job_seeker` UNIQUE (`job_id`, `job_seeker_id`)', 'SELECT 1');
PREPARE migration_stmt FROM @migration_sql;
EXECUTE migration_stmt;
DEALLOCATE PREPARE migration_stmt;
