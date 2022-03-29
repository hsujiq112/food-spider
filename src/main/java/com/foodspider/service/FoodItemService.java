package com.foodspider.service;

import com.foodspider.exception.FoodItemInUseExcepiton;
import com.foodspider.exception.MissingFoodItemException;
import com.foodspider.model.FoodItem;
import java.util.UUID;

public class FoodItemService extends ServiceBase<FoodItem> {

    @Override
    public void deleteByID(UUID id) {
        var foodItem = getByID(id);
        if (foodItem == null) {
            throw new MissingFoodItemException("Missing food item");
        }
        if (!foodItem.getOrders().isEmpty()) {
            throw new FoodItemInUseExcepiton(foodItem.getName() + " is used in some orders... you cannot delete it");
        }
        getRepo().deleteById(id);
    }
}
