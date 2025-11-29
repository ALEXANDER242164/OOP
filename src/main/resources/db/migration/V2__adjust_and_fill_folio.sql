-- Flyway migration V2: ensure folio is VARCHAR(6), fill missing folios with zero-padded ID, add unique index
-- Adds column if missing, adjusts column length, fills existing null/empty folios, and creates unique index
-- Flyway migration V2: adjust folio length to 6 and fill missing folios

-- If column missing (defensive), add it
SET @col_exists = (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'patients' AND COLUMN_NAME = 'folio'
);
SET @sql = IF(@col_exists = 0, 'ALTER TABLE patients ADD COLUMN folio VARCHAR(6) NULL', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Modify column type/length to VARCHAR(6) (if needed)
SET @modify_sql = 'ALTER TABLE patients MODIFY COLUMN folio VARCHAR(6) NULL';
PREPARE stmtm FROM @modify_sql;
EXECUTE stmtm;
DEALLOCATE PREPARE stmtm;

-- Populate missing folios using zero-padded patient id (6 digits)
UPDATE patients
SET folio = LPAD(CAST(id AS CHAR), 6, '0')
WHERE folio IS NULL OR folio = '';

-- Create unique index if missing
SET @idx_exists = (
  SELECT COUNT(*) FROM information_schema.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'patients' AND INDEX_NAME = 'idx_patients_folio'
);
SET @sql2 = IF(@idx_exists = 0, 'CREATE UNIQUE INDEX idx_patients_folio ON patients(folio)', 'SELECT 1');
PREPARE stmt2 FROM @sql2;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;
