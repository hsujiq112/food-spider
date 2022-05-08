package com.foodspider.service;

import com.foodspider.model.CategoryEnum;
import com.foodspider.model.FoodItem;
import com.foodspider.model.Restaurant;
import com.foodspider.model.narrowed_model.NarrowedRestaurant;
import com.foodspider.validator.FoodValidator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.hibernate.Hibernate;

import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * The type Restaurant service.
 */
public class RestaurantService extends ServiceBase<Restaurant> {



    public ArrayList<Restaurant> dbSet() {
        return new ArrayList<>(getRepo().findAll());
    }

    /**
     * Gets food items by restaurant id.
     *
     * @param id the id
     * @return the food items by restaurant id
     */
    public ArrayList<FoodItem> getFoodItemsByRestaurantID(UUID id) {
        return new ArrayList<>(getRepo().getById(id).getFoodItems());
    }

    /**
     * Gets categories by restaurant id.
     *
     * @param id the id
     * @return the categories by restaurant id
     */
    public ArrayList<Integer> getCategoriesByRestaurantID(UUID id) {
        return new ArrayList<>(getRepo().getById(id).getCategories());
    }

    /**
     * Add food to restaurant.
     *
     * @param restaurantID    the restaurant id
     * @param foodName        the food name
     * @param foodDescription the food description
     * @param price           the price
     * @param categoryEnum    the category enum
     * @param foodImageLink   the food image link
     */
    public void addFoodToRestaurant(UUID restaurantID,
                                    String foodName,
                                    String foodDescription,
                                    Double price,
                                    CategoryEnum categoryEnum,
                                    String foodImageLink) {
        var restaurant = getRepo().getById(restaurantID);
        FoodValidator.validateFood(foodName, foodDescription, price, categoryEnum, foodImageLink);
        var foodItem = new FoodItem(foodName, foodDescription, price, categoryEnum, foodImageLink);
        restaurant.getFoodItems().add(foodItem);
        foodItem.setRestaurant(restaurant);
        getRepo().save(restaurant);
    }

    /**
     * Gets narrowed restaurants.
     *
     * @param filter the filter
     * @return the narrowed restaurants
     */
    public ArrayList<NarrowedRestaurant> getNarrowedRestaurants(String filter) {
        var restaurants = dbSet().stream().map(i -> new NarrowedRestaurant(){{
            id = i.getId();
            name = i.getName();
            location = i.getLocation();
            deliveryZones = i.getDeliveryZones();
            categories = i.getCategories();
        }}).toList();
        if (filter != null) {
            restaurants = restaurants.stream().filter(i -> i.name.toLowerCase().contains(filter)).toList();
        }
        restaurants = restaurants.stream().sorted(Comparator.comparing(i -> i.name)).toList();
        return new ArrayList<>(restaurants);
    }
}
