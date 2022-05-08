package com.foodspider.service;

import com.foodspider.exception.InvalidUserException;
import com.foodspider.model.*;
import com.foodspider.model.narrowed_model.NarrowedFoodItem;
import com.foodspider.model.narrowed_model.NarrowedOrder;
import com.foodspider.model.response_model.GetOrdersCountByUserIDResponse;
import com.foodspider.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * The User base service.
 */
@Service
public class UserBaseService extends ServiceBase<UserBase> {

    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private CustomerService customerService;

    /**
     * Find by username.
     *
     * @param username the username
     * @return the optional userBase
     */
    public Optional<UserBase> getByUsername(String username) {
        return getRepo().findAll().stream()
                .filter(i -> i.getUsername().equals(username)).findFirst();
    }

    /**
     * Try login.
     *
     * @param username the username
     * @param password the password
     * @return the user base
     * @throws InvalidUserException in case the credentials fail
     * @throws Exception            general exception
     */
    public UserBase tryLogin(String username, String password) throws InvalidUserException, Exception {
        var user = getByUsername(username)
                .orElseThrow(() -> new InvalidUserException("Username or Password is incorrect"));
        var passwordDecrypted = Encryptor.decrypt(user.getId(), user.getPassword());
        if (!password.equals(passwordDecrypted)) {
            throw new InvalidUserException("Username or Password is incorrect");
        }
        return user;
    }

    /**
     * Try register user base.
     *
     * @param emailAddress the email address
     * @param firstName    the first name
     * @param lastName     the last name
     * @param username     the username
     * @param password     the password
     * @param isAdmin      the is admin
     * @return the newly added user that has been added to the database
     * @throws InvalidUserException in case the validation fails
     * @throws Exception            general exception
     */
    public UserBase tryRegister(String emailAddress,
                                String firstName,
                                String lastName,
                                String username,
                                String password,
                                Boolean isAdmin) throws InvalidUserException, Exception {
        UserValidator.validateUser(emailAddress, firstName, lastName, username, password);
        if (isAdmin) {
            var admin = new Administrator(emailAddress, firstName, lastName, username, password);
            return add(admin);
        }
        var customer = new Customer(emailAddress, firstName, lastName, username, password);
        return add(customer);
    }

    /**
     * Gets orders count by user id.
     *
     * @param id      the user id
     * @param isAdmin if the user is an admin
     * @return the orders count by user id
     */
    public GetOrdersCountByUserIDResponse getOrdersCountByUserID(UUID id,
                                                                 Boolean isAdmin) {
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

    /**
     * Gets orders by user id.
     *
     * @param id      the user id
     * @param isAdmin if the user is an admin
     * @param filter  the order status filter
     * @return the orders filtered
     */
    public List<NarrowedOrder> getOrdersByUserID(UUID id,
                                                 Boolean isAdmin,
                                                 Integer filter) {
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
        if (filter != -1) {
            orders = orders.stream().filter(i -> i.getOrderStatus()
                    .equals(OrderStatusEnum.values()[filter])).toList();
        }
        orders = orders.stream().sorted(Comparator.comparing(Order::getOrderStatus)
                        .thenComparing(i -> i.getRestaurant().getName())
                        .thenComparing(i -> i.getCustomer().getFirstName())
                        .thenComparing(i -> i.getFoodItems().stream()
                                .mapToDouble(FoodItem::getPrice).sum()))
                        .toList();
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
            restaurantName = i.getRestaurant().getName();
        }}).toList());
    }
}
