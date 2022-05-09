package com.foodspider.service;

import com.foodspider.exception.InvalidOrderChangeException;
import com.foodspider.exception.MissingCustomerException;
import com.foodspider.exception.MissingFoodItemException;
import com.foodspider.model.Order;
import com.foodspider.model.OrderStatusEnum;
import com.foodspider.model.request_model.AddOrderRequest;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * The Order service.
 */
public class OrderService extends ServiceBase<Order> {

    @Autowired
    @Setter
    private CustomerService customerService;
    @Setter
    @Autowired
    private FoodItemService foodItemService;
    @Setter
    @Autowired
    private RestaurantService restaurantService;
    @Setter
    @Autowired
    private MailService mailService;

    /**
     * Try add order.
     *
     * @param request the request
     * @throws Exception general exception
     * @throws MissingCustomerException if the customer is not in db
     * @throws MissingFoodItemException if the food item is not in db
     */
    public void tryAddOrder(AddOrderRequest request) throws Exception {
        var user = customerService.getByID(request.userId);
        if (user == null) {
            throw new MissingCustomerException("Customer not found");
        }
        if (request.foodItems == null || request.foodItems.isEmpty()) {
            throw new MissingFoodItemException("An order cannot have 0 food items!");
        }
        var fullFoodItems = request.foodItems.stream()
                .map(i -> foodItemService.getByID(i)).toList();
        var restaurant = restaurantService.getByID(request.restaurantId);
        var order = new Order(restaurant, user, fullFoodItems);
        user.getOrders().add(order);
        forceUpdate(order, order.getId());
        customerService.forceUpdate(user, request.userId);
        mailService.sendMail(request, user, restaurant);
    }

    /**
     * Change status to order.
     *
     * @param orderID the order id
     * @param status  the status
     * @throws InvalidOrderChangeException if the state is changed to an illegal one
     */
    public void changeStatusToOrder(UUID orderID,
                                    Integer status) {
        var order = getByID(orderID);
        if (order.getOrderStatus().equals(OrderStatusEnum.DECLINED)) {
            throw new InvalidOrderChangeException("Cannot change from Declined Order to something else!");
        }
        if (order.getOrderStatus().equals(OrderStatusEnum.DELIVERED)) {
            throw new InvalidOrderChangeException("Cannot change from Delivered Order to something else!");
        }
        order.setOrderStatus(OrderStatusEnum.values()[status]);
        update(order, orderID);
    }
}
