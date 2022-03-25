package com.foodspider.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orderuru")
public class Order {

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID orderID;

    @Column(nullable = false)
    private OrderStatusEnum orderStatus;

    @ManyToOne
    @JoinColumn(name = "customerID")
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private List<FoodItem> foodItem;

    public UUID getOrderID() {
        return orderID;
    }

    public OrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<FoodItem> getFoodItem() {
        return foodItem;
    }

    public Order(OrderStatusEnum orderStatus, List<FoodItem> foodItem) {
        this.orderID = UUID.randomUUID();
        this.orderStatus = orderStatus;
        this.foodItem = foodItem;
    }

    public Order() {

    }
}
