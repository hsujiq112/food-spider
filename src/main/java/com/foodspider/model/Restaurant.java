package com.foodspider.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID restaurantID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String deliveryZones;

    @OneToOne
    @JoinColumn(name = "administratorID")
    private Administrator administrator;

    public UUID getRestaurantID() {
        return restaurantID;
    }

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
