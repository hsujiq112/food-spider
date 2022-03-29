package com.foodspider.service;

import com.foodspider.exception.InvalidUserException;
import com.foodspider.model.*;
import com.foodspider.model.narrowed_model.NarrowedFoodItem;
import com.foodspider.model.narrowed_model.NarrowedOrder;
import com.foodspider.model.response_model.GetOrdersCountByUserIDResponse;
import com.foodspider.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserBaseService extends ServiceBase<UserBase> {

    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private CustomerService customerService;

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

    public GetOrdersCountByUserIDResponse getOrdersCountByUserID(UUID id, Boolean isAdmin) {
        List<Order> orders;
        if (isAdmin) {
            var restaurant = administratorService.getByID(id).getRestaurant();
            if (restaurant == null) {
                return new GetOrdersCountByUserIDResponse(){{
                    placedOrders = 0;
                    pendingOrders = 0;
                }};
            }
            orders = restaurant.getOrders();
        } else {
            orders = customerService.getByID(id).getOrders();
        }
        return new GetOrdersCountByUserIDResponse(){{
            placedOrders = orders.size();
            pendingOrders = orders.stream()
                    .filter(i -> i.getOrderStatus()
                            .equals(OrderStatusEnum.PENDING)).toList().size();
        }};
    }

    public List<NarrowedOrder> getOrdersByUserID(UUID id, Boolean isAdmin) {
        List<Order> orders;
        if (isAdmin) {
            var restaurant = administratorService.getByID(id).getRestaurant();
            if (restaurant == null) {
                return new ArrayList<>();
            }
            orders = restaurant.getOrders();
        } else {
            orders = customerService.getByID(id).getOrders();
        }
        return new ArrayList<>(orders.stream().map(i -> new NarrowedOrder(){{
            id = i.getId();
            foodItems = new ArrayList<>(i.getFoodItems().stream().map(j -> new NarrowedFoodItem(){{
                id = j.getId();
                name = j.getName();
                description = j.getDescription();
                price = j.getPrice();
                category = j.getCategory();
                imageLink = j.getImageLink();
            }}).toList());
            status = i.getOrderStatus();
            clientFirstName = i.getCustomer().getFirstName();
            clientLastName = i.getCustomer().getLastName();
        }}).toList());
    }
}
