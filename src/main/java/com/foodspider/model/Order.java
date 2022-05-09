package com.foodspider.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "orderuru")
public class Order extends BaseModel {

    @Setter
    @Column(nullable = false)
    private OrderStatusEnum orderStatus;

    @Setter
    @ManyToOne
    @JoinColumn(name = "customerID")
    private Customer customer;

    @Setter
    @ManyToOne
    @JoinColumn(name = "restaurantID")
    private Restaurant restaurant;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "food_item_order",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "food_item_id"))
    private List<FoodItem> foodItems;

    public Order(OrderStatusEnum orderStatus, List<FoodItem> foodItem) {
        this.setId(UUID.randomUUID());
        this.orderStatus = orderStatus;
        this.foodItems = foodItem;
    }

    public Order(OrderStatusEnum orderStatus) {
        this.setId(UUID.randomUUID());
        this.orderStatus = orderStatus;
    }

    public Order() {

    }

    public Order(Restaurant restaurant, Customer customer, List<FoodItem> foodItems) {
        this.setId(UUID.randomUUID());
        this.customer = customer;
        this.foodItems = foodItems;
        this.restaurant = restaurant;
        this.orderStatus = OrderStatusEnum.PENDING;
    }
}
