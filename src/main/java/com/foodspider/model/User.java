package com.foodspider.model;

import com.foodspider.service.Encryptor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID userId;

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

    @Column(nullable = false)
    private Boolean isAdmin;

    public User(String emailAddress, String firstName, String lastName, String username, String password) throws Exception {
        this.userId = UUID.randomUUID();
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = Encryptor.encrypt(userId, password);
        this.isAdmin = false;
    }

    public User(UUID userId, String emailAddress, String firstName, String lastName, String username, String password, Boolean isAdmin) {
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public User() {

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

    public Boolean getAdmin() {
        return isAdmin;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UUID getUserId() {
        return userId;
    }
}
