package com.foodspider.validator;

import com.foodspider.exception.InvalidRestaurantException;

public class RestaurantValidator {
    public static void validateRestaurant(String name, String location, String deliveryZones) {
        if (name.equals("")) {
            throw new InvalidRestaurantException("Empty restaurant name");
        }
        if (name.length() > 256) {
            throw new InvalidRestaurantException("Restaurant name exceeds 256 characters");
        }
        if (location.equals("")) {
            throw new InvalidRestaurantException("Empty restaurant location");
        }
        if (location.length() > 256) {
            throw new InvalidRestaurantException("Restaurant location exceeds 256 characters");
        }
        if (deliveryZones.equals("")) {
            throw new InvalidRestaurantException("Empty delivery zones");
        }
        if (deliveryZones.length() > 256) {
            throw new InvalidRestaurantException("Restaurant delivery zones exceeds 256 characters");
        }
    }

}