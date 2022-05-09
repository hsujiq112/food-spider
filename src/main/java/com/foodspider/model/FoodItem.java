package com.foodspider.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "food_item")
public class FoodItem extends BaseModel {

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

    @Setter
    @ManyToMany(mappedBy = "foodItems")
    private List<Order> orders;

    @ManyToOne
    @Setter
    @JoinColumn(name = "restaurantID")
    private Restaurant restaurant;

    public FoodItem(String name,
                    String description,
                    Double price,
                    CategoryEnum category,
                    String imageLink) {
        setId(UUID.randomUUID());
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageLink = imageLink;
    }

    public FoodItem(UUID id,
                    String name,
                    String description,
                    Double price,
                    CategoryEnum category,
                    String imageLink) {
        setId(id);
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageLink = imageLink;
    }

    public FoodItem() {

    }

    @Override
    public String toString() {
        return "name='" + name + "'" +
                ", description='" + description + "'" +
                ", price=" + price;
    }
}
