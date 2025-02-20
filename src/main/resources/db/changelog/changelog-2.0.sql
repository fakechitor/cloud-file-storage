--liquibase formatted sql

--changeset fakechitor:1
CREATE TABLE roles(
    id SERIAL PRIMARY KEY NOT NULL,
    role VARCHAR NOT NULL CHECK ( length(trim(role)) > 0 ) UNIQUE
);

--changeset fakechitor:2
CREATE TABLE user_roles(
    id SERIAL PRIMARY KEY NOT NULL,
    id_user INT NOT NULL REFERENCES users(id),
    id_role INT NOT NULL REFERENCES roles(id),
    CONSTRAINT unique_user_roles UNIQUE(id_user, id_role)
);
