package com.foodspider.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "food_item")
public class FoodItem {

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID foodItemID;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Double price;

    @Column
    private CategoryEnum category;

    @Column
    private String imageLink;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public UUID getFoodItemID() {
        return foodItemID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public String getImageLink() {
        return imageLink;
    }

    public Order getOrder() {
        return order;
    }
}
