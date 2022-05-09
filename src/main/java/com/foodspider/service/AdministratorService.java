package com.foodspider.service;

import com.foodspider.exception.InvalidRestaurantException;
import com.foodspider.exception.MissingAdministratorException;
import com.foodspider.model.Administrator;
import com.foodspider.model.Restaurant;
import com.foodspider.validator.RestaurantValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * The Administrator service.
 */
@Service
public class AdministratorService extends ServiceBase<Administrator> {

    /**
     * Trying to add restaurant to admin administrator.
     *
     * @param adminID                 the admin id
     * @param restaurantName          the restaurant name
     * @param restaurantLocation      the restaurant location
     * @param restaurantDeliveryZones the restaurant delivery zones
     * @param categories              the categories
     * @return the administrator saved in the database
     * @throws InvalidRestaurantException if the validation fails
     * @throws MissingAdministratorException if the administrator cannot be found in the db
     */
    public Administrator tryAddRestaurantToAdmin(UUID adminID,
                                                 String restaurantName,
                                                 String restaurantLocation,
                                                 String restaurantDeliveryZones,
                                                 List<Integer> categories) {
        var restaurant = new Restaurant(restaurantName, restaurantLocation, restaurantDeliveryZones, categories);
        RestaurantValidator.validateRestaurant(restaurantName, restaurantLocation, restaurantDeliveryZones, categories);
        var admin = getRepo().getById(adminID);
        if (admin == null) {
            throw new MissingAdministratorException("Administrator not found");
        }
        admin.setRestaurant(restaurant);
        restaurant.setAdministrator(admin);
        return getRepo().save(admin);
    }


    /**
     * Gets the restaurant by user id.
     *
     * @param id the user id
     * @return the restaurant found for the restaurant
     */
    public Restaurant getRestaurantByAdminID(UUID id) {
        var admin = getRepo().getById(id);
        if (admin == null) {
            throw new MissingAdministratorException("Administrator not found");
        }
        return admin.getRestaurant();
    }
}
