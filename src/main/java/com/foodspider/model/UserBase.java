package com.foodspider.model;

import com.foodspider.service.Encryptor;
import lombok.Getter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Table(name = "user_base")
public class UserBase extends BaseModel {

    @Column(nullable = false)
    private String emailAddress;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    public UserBase(String emailAddress,
                    String firstName,
                    String lastName,
                    String username,
                    String password) throws Exception {
        this.setId(UUID.randomUUID());
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = Encryptor.encrypt(getId(), password);
    }

    public UserBase() {

    }

}
