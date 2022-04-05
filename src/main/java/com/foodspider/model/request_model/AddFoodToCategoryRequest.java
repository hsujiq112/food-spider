package com.foodspider.model.request_model;

import com.foodspider.model.CategoryEnum;

import java.util.UUID;

public class AddFoodToCategoryRequest extends RequestBase {
    public UUID restaurantID;
    public String foodName;
    public String foodDescription;
    public Double price;
    public String foodImageLink;
    public CategoryEnum categoryEnum;
}
