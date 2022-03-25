package com.foodspider.service;

import com.foodspider.exception.InvalidUserException;
import com.foodspider.model.Customer;
import com.foodspider.model.Order;
import com.foodspider.model.OrderStatusEnum;
import com.foodspider.validator.UserValidator;

import java.util.ArrayList;
import java.util.Optional;

public class CustomerService extends ServiceBase<Customer> {

    public Optional<Customer> findByUsername(String username) {
        return getRepo().findAll().stream()
                .filter(i -> i.getUsername().equals(username)).findFirst();
    }

    public Customer tryLogin(String username, String password) throws Exception {
        var customer = findByUsername(username);
        if (customer.isEmpty()) {
            return null;
        }
        var passwordDecrypted = Encryptor.decrypt(customer.get().getId(), customer.get().getPassword());
        if (!password.equals(passwordDecrypted)) {
            return null;
        }
        return customer.get();
    }

    public Customer tryRegister(String emailAddress, String firstName, String lastName,
                                String username, String password) throws InvalidUserException, Exception {
        UserValidator.validateUser(emailAddress, firstName, lastName, username, password);
        var customer = new Customer(emailAddress, firstName, lastName, username, password);
        customer.getOrders().add(new Order(OrderStatusEnum.PENDING, new ArrayList<>()));
        return add(customer);
    }
}
