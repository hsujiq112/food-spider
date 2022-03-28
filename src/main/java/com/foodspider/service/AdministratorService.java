package com.foodspider.service;

import com.foodspider.exception.MissingAdministratorException;
import com.foodspider.model.Administrator;
import com.foodspider.model.Restaurant;
import com.foodspider.validator.RestaurantValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdministratorService extends ServiceBase<Administrator> {

    public Administrator tryAddRestaurantToAdmin(UUID adminID, String restaurantName,
                                                 String restaurantLocation, String restaurantDeliveryZones,
                                                 List<Integer> categories) {
        var restaurant = new Restaurant(restaurantName, restaurantLocation, restaurantDeliveryZones, categories);
        RestaurantValidator.validateRestaurant(restaurantName, restaurantLocation, restaurantDeliveryZones);
        var admin = getRepo().findById(adminID)
                .orElseThrow(() -> new MissingAdministratorException("Administrator not found"));
        admin.setRestaurant(restaurant);
        restaurant.setAdministrator(admin);
        return getRepo().save(admin);
    }


    public Restaurant getFoodItemsByRestaurantID(UUID id) {
        var admin = getRepo().findById(id)
                .orElseThrow(() -> new MissingAdministratorException("Administrator not found"));
        return admin.getRestaurant();
    }
}
