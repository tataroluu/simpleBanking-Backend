-- Account tablosuna başlangıç verileri
INSERT INTO account (owner, account_number, balance)
VALUES ('Kerem Karaca', '17892', 0.0);

INSERT INTO account (owner, account_number, balance)
VALUES ('Demet Demircan', '9834', 100.0);

INSERT INTO account (owner, account_number, balance)
VALUES ('Canan Kaya', '1234', 40.0);

-- Transaction tablosuna başlangıç verileri
-- Demet Demircan için işlemler
INSERT INTO transaction (type, amount, date, account_id)
VALUES ('CREDIT', 100.0, CURRENT_TIMESTAMP, 2);

INSERT INTO transaction (type, amount, date, account_id)
VALUES ('DEBIT', 50.0, CURRENT_TIMESTAMP, 2);

-- Canan Kaya için işlemler
INSERT INTO transaction (type, amount, date, account_id)
VALUES ('CREDIT', 100.0, CURRENT_TIMESTAMP, 3);

INSERT INTO transaction (type, amount, date, account_id)
VALUES ('DEBIT', 60.0, CURRENT_TIMESTAMP, 3);

INSERT INTO transaction (type, amount, date, account_id)
VALUES ('DEPOSIT', 60.0, CURRENT_TIMESTAMP, 3);