package com.foodspider.model.narrowed_model;

import com.foodspider.model.CategoryEnum;

import java.util.UUID;

public class NarrowedFoodItem {
    public UUID id;
    public String name;
    public String description;
    public Double price;
    public CategoryEnum category;
    public String imageLink;
}
