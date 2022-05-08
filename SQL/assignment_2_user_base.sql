create table user_base
(
    dtype         varchar(31)  not null,
    id            varchar(255) not null
        primary key,
    email_address varchar(255) not null,
    first_name    varchar(255) not null,
    last_name     varchar(255) not null,
    password      varchar(255) not null,
    username      varchar(255) not null,
    constraint UK_kahemtx24qrkfr91oaruh78an
        unique (username)
);

INSERT INTO assignment_2.user_base (dtype, id, email_address, first_name, last_name, password, username) VALUES ('Administrator', '0b91fad2-f567-4003-8eda-a66289beb9aa', 'Rares.Man@restaurant.ro', 'Rares Restaurant', 'Man Restaurant', 'MDuQjLvx65g=', 'raresman1');
INSERT INTO assignment_2.user_base (dtype, id, email_address, first_name, last_name, password, username) VALUES ('Customer', '28154ef5-8742-4414-b4e6-5357984ea4ae', 'Rares.Man@student.utcluj.ro', 'Rares', 'Man', 'fkpitazDisY=', 'raresman');
INSERT INTO assignment_2.user_base (dtype, id, email_address, first_name, last_name, password, username) VALUES ('Customer', '447ef6f5-23d6-46b8-b097-997b014e6265', 'ion@popescu.com', 'Ion', 'Popescu', 'Kl4dNyace4M=', 'ionpopescu');
INSERT INTO assignment_2.user_base (dtype, id, email_address, first_name, last_name, password, username) VALUES ('Administrator', '8dceefc1-3dc7-429e-bbc2-364c9a8fd3a7', 'restaurant@random.email', 'Alt Restaurant', 'Pentru ca Da', 'jVv+0i4f2b8=', 'raresman2');
INSERT INTO assignment_2.user_base (dtype, id, email_address, first_name, last_name, password, username) VALUES ('Administrator', 'b949752e-1f41-4397-8f26-456e15bf6d10', 'alt@restaurant.altex', 'Iara un Restaurant', 'Pentru ca Nu', 'H+MDXqcYedA=', 'raresman3');
