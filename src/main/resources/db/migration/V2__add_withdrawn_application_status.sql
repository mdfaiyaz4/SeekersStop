-- Existing databases are baselined at version 1. Fresh databases have no
-- application table yet; Hibernate creates it with the current enum afterwards.
SET @application_status_column_type = (
    SELECT COLUMN_TYPE
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'application'
      AND COLUMN_NAME = 'status'
    LIMIT 1
);

SET @application_status_migration_sql = IF(
    @application_status_column_type IS NULL
        OR LOCATE('WITHDRAWN', UPPER(@application_status_column_type)) > 0,
    'SELECT 1',
    'ALTER TABLE `application` MODIFY COLUMN `status` ENUM(''ACCEPTED'', ''PENDING'', ''REJECTED'', ''SHORTLISTED'', ''WITHDRAWN'') NULL DEFAULT NULL'
);

PREPARE application_status_migration FROM @application_status_migration_sql;
EXECUTE application_status_migration;
DEALLOCATE PREPARE application_status_migration;
