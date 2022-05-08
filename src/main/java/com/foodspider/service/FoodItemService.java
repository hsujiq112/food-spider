package com.foodspider.service;

import com.foodspider.exception.FoodItemInUseException;
import com.foodspider.exception.MissingFoodItemException;
import com.foodspider.model.FoodItem;
import com.foodspider.model.OrderStatusEnum;

import java.util.UUID;

/**
 * The Food item service.
 */
public class FoodItemService extends ServiceBase<FoodItem> {


    /**
     * Overridden deletion of a foodItem by id
     * @param id the foodItem id
     * @throws FoodItemInUseException if the food item exists in orders
     * @throws MissingFoodItemException when the id is erroneous
     */
    @Override
    public void deleteByID(UUID id) {
        var foodItem = getByID(id);
        if (foodItem == null) {
            throw new MissingFoodItemException("Missing food item");
        }
        if (!foodItem.getOrders().stream()
                .filter(i -> !i.getOrderStatus()
                        .equals(OrderStatusEnum.DELIVERED)).toList()
                .isEmpty()) {
            throw new FoodItemInUseException(foodItem.getName() + " is used in some orders... you cannot delete it");
        }
        getRepo().deleteById(id);
    }
}
