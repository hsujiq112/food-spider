package com.foodspider.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "administrator")
public class Administrator extends UserBase {

    @OneToOne(mappedBy = "administrator", cascade = CascadeType.ALL)
    private Restaurant restaurant;

    public Restaurant getRestaurant() {
        return restaurant;
    }

}
