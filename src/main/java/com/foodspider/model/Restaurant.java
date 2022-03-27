package com.foodspider.model;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "restaurant")
public class Restaurant extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String deliveryZones;

    @OneToOne
    @JoinColumn(name = "administratorID")
    private Administrator administrator;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<FoodItem> foodItems;

    public Restaurant(String name, String location, String deliveryZones) {
        this.name = name;
        this.location = location;
        this.deliveryZones = deliveryZones;
        this.foodItems = new ArrayList<>();
    }

    public Restaurant() {

    }
}
