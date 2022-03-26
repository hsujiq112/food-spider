package com.foodspider.model;

import com.foodspider.service.Encryptor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_base")
public class UserBase extends BaseModel{

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

    public UserBase(String emailAddress, String firstName, String lastName, String username, String password) throws Exception {
        this.setId(UUID.randomUUID());
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = Encryptor.encrypt(getId(), password);
    }

    public UserBase(UUID userId, String emailAddress, String firstName, String lastName, String username, String password) {
        this.setId(userId);
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public UserBase() {

    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
