create table orderuru
(
    id           varchar(255) not null
        primary key,
    order_status int          not null,
    customerid   varchar(255) null,
    constraint FKdft8fs2063hnqxdjh3cpj8mox
        foreign key (customerid) references user_base (id)
);

INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('2cec1a5e-50e1-4fbe-a4e6-f1bfb3341263', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('2ffed74a-61b1-4188-9a4e-31474da74bbb', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('34e5ebc2-16ca-40d0-b54b-51e948aa8fc8', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('3c06c72f-c188-49fc-bf45-45d137079d01', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('449272d1-a340-48c5-832c-051983b4b272', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('7d6f1967-cecc-4919-8f11-fc5cd87547d6', 3, '28154ef5-8742-4414-b4e6-5357984ea4ae', '9b1adb08-11fc-4fd7-9e55-24a3ea1490ab');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('83d68c53-e7a3-4bf1-b94a-31663ecdd422', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('8b64f977-d643-424d-bc8a-9fb738c1d131', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('a8fa64df-7fde-4fa0-9d53-a7043f7bf173', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('ae02953d-b1b1-464b-9502-f472f3dead61', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('ae4840fd-6113-481a-b7c8-3f52024e4d49', 0, '447ef6f5-23d6-46b8-b097-997b014e6265', 'bf2334cb-e67a-4ac1-90c1-003a221adc80');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('b774b622-4fae-40d9-8339-1576066ac8b5', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('c31810b8-28c4-42c3-8e4f-8f21416346de', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('c632d0aa-fa35-4db3-a92c-878ef301dff6', 0, '447ef6f5-23d6-46b8-b097-997b014e6265', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('c88bba87-7ca2-409a-896a-4a9aff133ab7', 0, '447ef6f5-23d6-46b8-b097-997b014e6265', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('cdeb81c1-a741-4581-ac48-17b709d81ae7', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('cfb3d798-55f6-4cdd-b189-b7cfa06a7fcd', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('d7c22121-20eb-461e-a394-364c9edf126d', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'bf2334cb-e67a-4ac1-90c1-003a221adc80');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('e688837c-95f8-4708-acb6-e3a3bf4cbaa5', 0, '28154ef5-8742-4414-b4e6-5357984ea4ae', 'c810cf20-e9d2-49f3-a9df-98e8e9b6f30c');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('f4bceb2d-b733-4d2b-af16-e09ea977a9eb', 0, '447ef6f5-23d6-46b8-b097-997b014e6265', '9b1adb08-11fc-4fd7-9e55-24a3ea1490ab');
INSERT INTO assignment_2.orderuru (id, order_status, customerid, restaurantid) VALUES ('fa0830fb-51ad-42d9-a5cc-8474d47aa792', 4, '28154ef5-8742-4414-b4e6-5357984ea4ae', '9b1adb08-11fc-4fd7-9e55-24a3ea1490ab');
