package com.foodspider.model.response_model;

import com.foodspider.model.narrowed_model.NarrowedFoodItem;

import java.util.List;

public class MenuItemsResponse extends ResponseBase{
    public List<NarrowedFoodItem> foodItems;
    public List<Integer> categories;
}
