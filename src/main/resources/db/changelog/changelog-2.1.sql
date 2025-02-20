--liquibase formatted sql

--changeset fakechitor:1
INSERT INTO roles(id, role)
VALUES
    (1, 'ROLE_USER'),
    (2, 'ROLE_ADMIN');