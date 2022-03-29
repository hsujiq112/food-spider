package com.foodspider.service;

import com.foodspider.exception.MissingCustomerException;
import com.foodspider.model.Order;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

public class OrderService extends ServiceBase<Order> {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private FoodItemService foodItemService;

    public void tryAddOrder(UUID userId, List<UUID> foodItems) {
        var user = customerService.getByID(userId);
        if (user == null) {
            throw new MissingCustomerException("Customer not found");
        }
        var fullFoodItems = foodItems.stream()
                .map(i -> foodItemService.getByID(i)).toList();
        var order = new Order(user, fullFoodItems);
        user.getOrders().add(order);
        forceUpdate(order, order.getId());
        customerService.forceUpdate(user, userId);
    }
}
