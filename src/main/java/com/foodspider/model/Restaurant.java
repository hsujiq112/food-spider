package com.foodspider.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "restaurant")
public class Restaurant extends BaseModel {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String deliveryZones;

    @ElementCollection
    @Column(nullable = false)
    private List<Integer> categories;

    @OneToOne
    @Setter
    @JoinColumn(name = "administratorID")
    private Administrator administrator;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<FoodItem> foodItems;

    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders;

    public Restaurant(String name,
                      String location,
                      String deliveryZones,
                      List<Integer> categories) {
        this.setId(UUID.randomUUID());
        this.name = name;
        this.location = location;
        this.deliveryZones = deliveryZones;
        this.categories = categories;
        this.foodItems = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    public Restaurant(UUID restaurantID,
                      String name,
                      String location,
                      String deliveryZones,
                      List<Integer> categories) {
        this.setId(restaurantID);
        this.name = name;
        this.location = location;
        this.deliveryZones = deliveryZones;
        this.categories = categories;
        this.foodItems = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    public Restaurant() {

    }

    @Override
    public String toString() {
        var ref = new Object() {
            String restaurantString = "";
        };
        ref.restaurantString += "Restaurant name: " + name + '\r';
        ref.restaurantString += "Restaurant location: " + location + '\r';
        ref.restaurantString += "Restaurant delivery zones: " + deliveryZones + '\r';
        var foodItemsGrouped = foodItems.stream().collect(Collectors.groupingBy(FoodItem::getCategory));
        ref.restaurantString += "Menu:\r\r";
        foodItemsGrouped.forEach((k, v) -> {
            ref.restaurantString += "  " + k.toString();
            v.forEach(i -> ref.restaurantString += "\r    " + i.toString());
            ref.restaurantString += "\r\r";
        });
        return ref.restaurantString;
    }
}
