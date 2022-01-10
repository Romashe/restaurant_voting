INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('User', 'user@mail.com', '{noop}password'),
       ('Admin', 'admin@mail.com', '{noop}admin');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);