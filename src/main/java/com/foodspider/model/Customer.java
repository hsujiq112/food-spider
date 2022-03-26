package com.foodspider.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customer")
public class Customer extends UserBase {

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<Order> orders;

    public Customer(String emailAddress, String firstName, String lastName, String username, String password) throws Exception {
        super(emailAddress, firstName, lastName, username, password);
        orders = new ArrayList<>();
    }

    public Customer() {

    }

    public List<Order> getOrders() {
        return orders;
    }


}
