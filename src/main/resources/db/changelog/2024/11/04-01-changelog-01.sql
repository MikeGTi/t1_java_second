-- liquibase formatted sql
-- changeset init
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS client
(   client_uuid UUID DEFAULT gen_random_uuid() PRIMARY KEY NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    middle_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS account (
   account_uuid UUID DEFAULT gen_random_uuid() PRIMARY KEY NOT NULL,
   client_uuid UUID REFERENCES client (client_uuid),
   account_type VARCHAR(16),
   balance DECIMAL(19,2),
   status VARCHAR(16) NOT NULL,
   frozen_amount DECIMAL(19, 2)
);

CREATE TABLE IF NOT EXISTS tbl_transaction (
    transaction_uuid UUID DEFAULT gen_random_uuid() PRIMARY KEY NOT NULL,
    client_uuid UUID REFERENCES client (client_uuid),
    account_uuid UUID REFERENCES account (account_uuid),
    amount DECIMAL(19,2),
    transaction_time TIMESTAMPTZ,
    status VARCHAR(16) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS data_source_error_log_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS data_source_error_log (
   id BIGINT NOT NULL,
   CONSTRAINT pk_data_source_error_log PRIMARY KEY (id),
   trace VARCHAR,
   message VARCHAR,
   method_signature VARCHAR
);