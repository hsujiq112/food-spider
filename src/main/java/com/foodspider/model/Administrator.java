package com.foodspider.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Administrator extends UserBase {

    @OneToOne(mappedBy = "administrator", cascade = CascadeType.ALL)
    private Restaurant restaurant;

    public Administrator(String emailAddress,
                         String firstName,
                         String lastName,
                         String username,
                         String password) throws Exception {
        super(emailAddress, firstName, lastName, username, password);
    }

    public Administrator() {

    }

}
