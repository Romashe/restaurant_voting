INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('User', 'user@mail.com', '{noop}password'),
       ('Admin', 'admin@mail.com', '{noop}admin');


INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (NAME)
VALUES ('Sahalin'),
       ('Metropol'),
       ('u Ashota');

INSERT INTO MENU_ITEM (NAME, PRICE, ITEM_DATE, RESTAURANT_ID)
VALUES ('Sibas', 100.25, CURRENT_DATE(), 1),
       ('SibasOld', 100.25, CURRENT_DATE() - 1, 1),
       ('Midii', 45.25, CURRENT_DATE(), 1),
       ('MidiiOld', 45.25, CURRENT_DATE() - 1, 1),
       ('Olivie', 1000.05, CURRENT_DATE(), 2),
       ('OlivieOld', 1000.05, CURRENT_DATE() - 1, 2),
       ('Sup', 67.05, CURRENT_DATE(), 2),
       ('SupOld', 67.05, CURRENT_DATE() - 1, 2),
       ('Lulya Kebab', 38, CURRENT_DATE(), 3),
       ('Lulya KebabOld', 38, CURRENT_DATE() - 1, 3),
       ('Shaverma', 10.05, CURRENT_DATE(), 3),
       ('ShavermaOld', 10.05, CURRENT_DATE() - 1, 3);

INSERT INTO VOTE (USER_ID, RESTAURANT_ID, VOTE_DATE)
VALUES (1, 1, CURRENT_DATE()),
       (1, 1, CURRENT_DATE()-1),
       (2, 1, CURRENT_DATE()),
       (2, 2, CURRENT_DATE()-1);