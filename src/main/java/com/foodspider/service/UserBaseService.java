package com.foodspider.service;

import com.foodspider.exception.InvalidUserException;
import com.foodspider.model.Administrator;
import com.foodspider.model.Customer;
import com.foodspider.model.UserBase;
import com.foodspider.validator.UserValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserBaseService extends ServiceBase<UserBase> {

    public Optional<UserBase> findByUsername(String username) {
        var test = getRepo().findAll();
        return getRepo().findAll().stream()
                .filter(i -> i.getUsername().equals(username)).findFirst();
    }

    public UserBase tryLogin(String username, String password) throws InvalidUserException, Exception {
        var user = findByUsername(username);
        if (user.isEmpty()) {
            throw new InvalidUserException("Username or Password is incorrect");
        }
        var passwordDecrypted = Encryptor.decrypt(user.get().getId(), user.get().getPassword());
        if (!password.equals(passwordDecrypted)) {
            throw new InvalidUserException("Username or Password is incorrect");
        }
        return user.get();
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

}
