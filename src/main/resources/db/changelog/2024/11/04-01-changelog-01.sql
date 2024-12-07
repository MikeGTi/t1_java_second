-- liquibase formatted sql
-- changeset init
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE SEQUENCE IF NOT EXISTS client_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS client (
    id BIGINT NOT NULL,
    CONSTRAINT pk_client PRIMARY KEY (id),
    client_uuid UUID DEFAULT gen_random_uuid(),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    middle_name VARCHAR(50)
);

CREATE SEQUENCE IF NOT EXISTS account_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS account (
    id BIGINT NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (id),
    account_uuid UUID DEFAULT gen_random_uuid(),
    client_id BIGINT REFERENCES client (id),
    account_type VARCHAR(16),
    balance DECIMAL(19,2),
    status VARCHAR(16) NOT NULL,
    frozen_amount DECIMAL(19, 2)
);

CREATE SEQUENCE IF NOT EXISTS transaction_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS tbl_transaction (
    id BIGINT NOT NULL,
    CONSTRAINT pk_transaction PRIMARY KEY (id),
    transaction_uuid UUID DEFAULT gen_random_uuid(),
    client_id BIGINT REFERENCES client (id),
    account_id BIGINT REFERENCES account (id),
    amount DECIMAL(19,2),
    created TIMESTAMPTZ NOT NULL,
    status VARCHAR(16) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS data_source_error_log_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS data_source_error_log (
   id BIGINT NOT NULL,
   CONSTRAINT pk_data_source_error_log PRIMARY KEY (id),
   trace VARCHAR,
   message VARCHAR,
   method_signature VARCHAR,
   created TIMESTAMPTZ NOT NULL
);