package com.foodspider.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
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

    @ManyToMany(mappedBy = "foodItems")
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "restaurantID")
    private Restaurant restaurant;

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

    public List<Order> getOrder() {
        return orders;
    }
}
