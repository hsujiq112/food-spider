package com.foodspider.model;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "orderuru")
public class Order extends BaseModel {

    @Column(nullable = false)
    private OrderStatusEnum orderStatus;

    @ManyToOne
    @JoinColumn(name = "customerID")
    private Customer customer;

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

    public Order() {

    }
}