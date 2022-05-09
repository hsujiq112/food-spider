package com.foodspider.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Customer extends UserBase {

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<Order> orders;

    public Customer(String emailAddress,
                    String firstName,
                    String lastName,
                    String username,
                    String password) throws Exception {
        super(emailAddress, firstName, lastName, username, password);
        orders = new ArrayList<>();
    }

    public Customer() {

    }

}
