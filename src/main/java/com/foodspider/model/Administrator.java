package com.foodspider.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name = "administrator")
public class Administrator extends UserBase {

    @OneToOne(mappedBy = "administrator", cascade = CascadeType.ALL)
    private Restaurant restaurant;

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Administrator(String emailAddress, String firstName, String lastName, String username, String password) throws Exception {
        super(emailAddress, firstName, lastName, username, password);
    }

    public Administrator() {

    }

}
