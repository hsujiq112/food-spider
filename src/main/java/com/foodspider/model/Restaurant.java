package com.foodspider.model;

import javax.persistence.*;
import java.util.List;

@Entity
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

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDeliveryZones() {
        return deliveryZones;
    }

    public Administrator getAdministrator() {
        return administrator;
    }
}
