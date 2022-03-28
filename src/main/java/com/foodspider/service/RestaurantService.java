package com.foodspider.service;

import com.foodspider.model.CategoryEnum;
import com.foodspider.model.FoodItem;
import com.foodspider.model.Restaurant;
import com.foodspider.validator.FoodValidator;

import java.util.ArrayList;
import java.util.UUID;

public class RestaurantService extends ServiceBase<Restaurant> {

    public ArrayList<Restaurant> dbSet() {
        return new ArrayList<>(getRepo().findAll());
    }

    public ArrayList<FoodItem> getFoodItemsByRestaurantID(UUID id) {
        return new ArrayList<>(getRepo().getById(id).getFoodItems());
    }

    public ArrayList<Integer> getCategoriesByRestaurantID(UUID id) {
        return new ArrayList<>(getRepo().getById(id).getCategories());
    }

    public void addFoodToRestaurant(UUID restaurantID, String foodName, String foodDescription, Double price,
                                    CategoryEnum categoryEnum, String foodImageLink) {
        var restaurant = getRepo().getById(restaurantID);
        FoodValidator.validateFood(foodName, foodDescription, price, categoryEnum, foodImageLink);
        var foodItem = new FoodItem(foodName, foodDescription, price, categoryEnum, foodImageLink);
        restaurant.getFoodItems().add(foodItem);
        foodItem.setRestaurant(restaurant);
        getRepo().save(restaurant);
    }
}
