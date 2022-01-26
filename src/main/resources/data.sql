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
VALUES ('Sibas', 10025, CURRENT_DATE, 1),
       ('SibasOld', 10025, CURRENT_DATE - 1, 1),
       ('Midii', 4525, CURRENT_DATE, 1),
       ('MidiiOld', 4525, CURRENT_DATE - 1, 1),
       ('Olivie', 100005, CURRENT_DATE, 2),
       ('OlivieOld', 100005, CURRENT_DATE - 1, 2),
       ('Sup', 6705, CURRENT_DATE, 2),
       ('SupOld', 6705, CURRENT_DATE - 1, 2),
       ('Lulya Kebab', 3800, CURRENT_DATE, 3),
       ('Lulya KebabOld', 3800, CURRENT_DATE - 1, 3),
       ('Shaverma', 1005, CURRENT_DATE, 3),
       ('ShavermaOld', 1005, CURRENT_DATE - 1, 3);

INSERT INTO VOTE (USER_ID, RESTAURANT_ID, VOTE_DATE)
VALUES (1, 1, CURRENT_DATE),
       (2, 2, CURRENT_DATE - 1),
       (1, 2, CURRENT_DATE - 1);