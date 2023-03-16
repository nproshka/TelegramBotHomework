-- liquibase formatted sql

--changeset prisStep: 1

CREATE TABLE notification_task_five
(
    id       bigserial,
    chat_id  bigint,
    message  text,
    date_and_time TIMESTAMP

);








