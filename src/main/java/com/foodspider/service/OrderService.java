package com.foodspider.service;

import com.foodspider.exception.MissingCustomerException;
import com.foodspider.model.Order;
import com.foodspider.model.OrderStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

public class OrderService extends ServiceBase<Order> {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private FoodItemService foodItemService;
    @Autowired
    private RestaurantService restaurantService;

    public void tryAddOrder(UUID restaurantId, UUID userId, List<UUID> foodItems) {
        var user = customerService.getByID(userId);
        if (user == null) {
            throw new MissingCustomerException("Customer not found");
        }
        var fullFoodItems = foodItems.stream()
                .map(i -> foodItemService.getByID(i)).toList();
        var restaurant = restaurantService.getByID(restaurantId);
        var order = new Order(restaurant, user, fullFoodItems);
        user.getOrders().add(order);
        forceUpdate(order, order.getId());
        customerService.forceUpdate(user, userId);
    }

    public void changeStatusToOrder(UUID orderID, Integer status) {
        var order = getByID(orderID);
        order.setOrderStatus(OrderStatusEnum.values()[status]);
        update(order, orderID);
    }
}
