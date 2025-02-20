--liquibase formatted sql

--changeset fakechitor:1
CREATE SEQUENCE user_id_seq
    START WITH 1
    INCREMENT BY 1
    CACHE 1;

--changeset fakechitor:2
CREATE TABLE users (
   id BIGINT PRIMARY KEY DEFAULT nextval('user_id_seq'),
   login VARCHAR(30) UNIQUE NOT NULL,
   password VARCHAR(255) NOT NULL
       CHECK (length(password) >= 3 AND length(password) <= 255)
);