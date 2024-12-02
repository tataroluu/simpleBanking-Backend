-- Account tablosuna başlangıç verileri
INSERT INTO account (owner, account_number, balance)
VALUES ('Kerem Karaca', '17892', 1000.0);

INSERT INTO account (account_number, balance, owner)
VALUES ('12345', 500.0, 'Ayşe Yılmaz');

INSERT INTO transaction (type, amount, date, account_id)
VALUES ('CREDIT', 1000.0, CURRENT_TIMESTAMP, 1);

INSERT INTO transaction (type, amount, date, account_id)
VALUES ('DEBIT', 50.0, CURRENT_TIMESTAMP, 1);

INSERT INTO transaction (type, amount, date, account_id)
VALUES ('CREDIT', 300.0, CURRENT_TIMESTAMP, 2);

INSERT INTO transaction (type, amount, date, account_id)
VALUES ('DEBIT', 100.0, CURRENT_TIMESTAMP, 2);