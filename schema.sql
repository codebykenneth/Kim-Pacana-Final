CREATE DATABASE IF NOT EXISTS ledger_db;
USE ledger_db;

CREATE TABLE IF NOT EXISTS journal_entries (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    entry_date   DATE          NOT NULL,
    account_name VARCHAR(100)  NOT NULL,
    description  VARCHAR(255),
    debit        DECIMAL(12,2) NOT NULL DEFAULT 0,
    credit       DECIMAL(12,2) NOT NULL DEFAULT 0
);

INSERT INTO journal_entries (entry_date, account_name, description, debit, credit) VALUES
('2026-10-01', 'Cash',             'Owner investment',      50000, 0),
('2026-10-01', 'Capital',          'Owner investment',      0, 50000),
('2026-10-03', 'Equipment',        'Bought printer',        12000, 0),
('2026-10-03', 'Cash',             'Bought printer',        0, 12000),
('2026-10-05', 'Cash',             'Service income',        8000, 0),
('2026-10-05', 'Service Revenue',  'Service income',        0, 8000),
('2026-10-10', 'Rent Expense',     'October rent',          5000, 0),
('2026-10-10', 'Cash',             'October rent',          0, 5000);
