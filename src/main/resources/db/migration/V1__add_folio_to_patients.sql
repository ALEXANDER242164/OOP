-- Flyway migration V1: add folio column to patients table if missing (MySQL compatible)

-- Add column only if it does not exist
SET @col_exists = (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'patients' AND COLUMN_NAME = 'folio'
);
SET @sql = IF(@col_exists = 0, 'ALTER TABLE patients ADD COLUMN folio VARCHAR(6) NULL', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Create unique index only if it does not exist
SET @idx_exists = (
  SELECT COUNT(*) FROM information_schema.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'patients' AND INDEX_NAME = 'idx_patients_folio'
);
SET @sql2 = IF(@idx_exists = 0, 'CREATE UNIQUE INDEX idx_patients_folio ON patients(folio)', 'SELECT 1');
PREPARE stmt2 FROM @sql2;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;
