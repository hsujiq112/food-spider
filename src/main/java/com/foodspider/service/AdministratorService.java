package com.foodspider.service;

import com.foodspider.exception.MissingAdministratorException;
import com.foodspider.model.Administrator;
import com.foodspider.model.Restaurant;
import com.foodspider.validator.RestaurantValidator;

import java.util.UUID;

public class AdministratorService extends ServiceBase<Administrator> {
    public Administrator tryAddRestaurantToAdmin(UUID adminID, String restaurantName,
                                   String restaurantLocation, String restaurantDeliveryZones) {
        var restaurant = new Restaurant(restaurantName, restaurantLocation, restaurantDeliveryZones);
        RestaurantValidator.validateRestaurant(restaurantName, restaurantLocation, restaurantDeliveryZones);
        var admin = getRepo().findById(adminID)
                .orElseThrow(() -> new MissingAdministratorException("Administrator not found"));
        admin.setRestaurant(restaurant);
        return getRepo().save(admin);
    }
}
