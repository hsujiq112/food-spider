package com.foodspider.service;

import com.foodspider.exception.InvalidUserException;
import com.foodspider.exception.MissingAdministratorException;
import com.foodspider.model.*;
import com.foodspider.validator.RestaurantValidator;
import com.foodspider.validator.UserValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserBaseService extends ServiceBase<UserBase> {

    public Optional<UserBase> findByUsername(String username) {
        return getRepo().findAll().stream()
                .filter(i -> i.getUsername().equals(username)).findFirst();
    }

    public UserBase tryLogin(String username, String password) throws InvalidUserException, Exception {
        var user = findByUsername(username)
                .orElseThrow(() -> new InvalidUserException("Username or Password is incorrect"));
        var passwordDecrypted = Encryptor.decrypt(user.getId(), user.getPassword());
        if (!password.equals(passwordDecrypted)) {
            throw new InvalidUserException("Username or Password is incorrect");
        }
        return user;
    }

    public UserBase tryRegister(String emailAddress, String firstName, String lastName,
                                String username, String password, Boolean isAdmin) throws InvalidUserException, Exception {
        UserValidator.validateUser(emailAddress, firstName, lastName, username, password);
        if (isAdmin) {
            var admin = new Administrator(emailAddress, firstName, lastName, username, password);
            return add(admin);
        }
        var customer = new Customer(emailAddress, firstName, lastName, username, password);
        return add(customer);
    }

    public List<Order> getOrdersByUserID(UUID id) {
        List<Order> orders;
        var user = getByID(id);
        if (user instanceof Administrator) {
            orders = ((Administrator) user).getRestaurant().getOrders();
        } else {
            orders = ((Customer) user).getOrders();
        }
        return orders;
    }


}
