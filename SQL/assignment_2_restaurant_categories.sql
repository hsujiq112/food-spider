create table restaurant_categories
(
    restaurant_id varchar(255) not null,
    categories    int          not null,
    constraint FKqtq0jri202b1jscqcpp8pywt2
        foreign key (restaurant_id) references restaurant (id)
);

INSERT INTO assignment_2.restaurant_categories (restaurant_id, categories) VALUES ('9b1adb08-11fc-4fd7-9e55-24a3ea1490ab', 0);
INSERT INTO assignment_2.restaurant_categories (restaurant_id, categories) VALUES ('9b1adb08-11fc-4fd7-9e55-24a3ea1490ab', 1);
INSERT INTO assignment_2.restaurant_categories (restaurant_id, categories) VALUES ('9b1adb08-11fc-4fd7-9e55-24a3ea1490ab', 2);
INSERT INTO assignment_2.restaurant_categories (restaurant_id, categories) VALUES ('9b1adb08-11fc-4fd7-9e55-24a3ea1490ab', 3);
INSERT INTO assignment_2.restaurant_categories (restaurant_id, categories) VALUES ('c810cf20-e9d2-49f3-a9df-98e8e9b6f30c', 1);
INSERT INTO assignment_2.restaurant_categories (restaurant_id, categories) VALUES ('c810cf20-e9d2-49f3-a9df-98e8e9b6f30c', 3);
INSERT INTO assignment_2.restaurant_categories (restaurant_id, categories) VALUES ('c810cf20-e9d2-49f3-a9df-98e8e9b6f30c', 4);
INSERT INTO assignment_2.restaurant_categories (restaurant_id, categories) VALUES ('bf2334cb-e67a-4ac1-90c1-003a221adc80', 1);
INSERT INTO assignment_2.restaurant_categories (restaurant_id, categories) VALUES ('bf2334cb-e67a-4ac1-90c1-003a221adc80', 2);
INSERT INTO assignment_2.restaurant_categories (restaurant_id, categories) VALUES ('bf2334cb-e67a-4ac1-90c1-003a221adc80', 3);
