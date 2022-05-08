create table restaurant
(
    id              varchar(255) not null
        primary key,
    delivery_zones  varchar(255) not null,
    location        varchar(255) not null,
    name            varchar(255) not null,
    administratorid varchar(255) null,
    constraint UK_i6u3x7opncroyhd755ejknses
        unique (name),
    constraint FKgaskqsam9h84yrykd3twh384i
        foreign key (administratorid) references user_base (id)
);

INSERT INTO assignment_2.restaurant (id, delivery_zones, location, name, administratorid) VALUES ('9b1adb08-11fc-4fd7-9e55-24a3ea1490ab', 'Marasti,Manastur,Gheorgheni', 'Memo catva ¯\\_(:D)_/¯', 'Marty', '0b91fad2-f567-4003-8eda-a66289beb9aa');
INSERT INTO assignment_2.restaurant (id, delivery_zones, location, name, administratorid) VALUES ('bf2334cb-e67a-4ac1-90c1-003a221adc80', 'Intre Lacuri,Marasti,Gheorgheni', 'In Iulius, Logic', 'KFC Iulius', 'b949752e-1f41-4397-8f26-456e15bf6d10');
INSERT INTO assignment_2.restaurant (id, delivery_zones, location, name, administratorid) VALUES ('c810cf20-e9d2-49f3-a9df-98e8e9b6f30c', 'Peste,Tot,In,Cluj', 'Nush, e pe tazz numa ', 'Grande Pizza', '8dceefc1-3dc7-429e-bbc2-364c9a8fd3a7');
