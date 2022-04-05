package com.foodspider.controller;

import com.foodspider.exception.InvalidRestaurantException;
import com.foodspider.exception.MissingAdministratorException;
import com.foodspider.model.Administrator;
import com.foodspider.model.FoodItem;
import com.foodspider.model.Restaurant;
import com.foodspider.model.narrowed_model.NarrowedFoodItem;
import com.foodspider.model.narrowed_model.NarrowedRestaurant;
import com.foodspider.model.request_model.AddRestaurantRequest;
import com.foodspider.model.response_model.*;
import com.foodspider.service.AdministratorService;
import com.foodspider.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class RestaurantController extends ControllerBase {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private AdministratorService administratorService;

    @GetMapping("/restaurants")
    public ResponseEntity<ResponseBase> getRestaurants() {
        ArrayList<NarrowedRestaurant> restaurantsList;
        try {
            restaurantsList = restaurantService.getNarrowedRestaurants(null);
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createOKResponse(new GetRestaurantsResponse(){{
            restaurants = restaurantsList;
        }});
    }

    @GetMapping("/restaurants/{filter}")
    public ResponseEntity<ResponseBase> getRestaurants(@PathVariable String filter) {
        ArrayList<NarrowedRestaurant> restaurantsList;
        try {
            restaurantsList = restaurantService.getNarrowedRestaurants(filter);
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createOKResponse(new GetRestaurantsResponse(){{
            restaurants = restaurantsList;
        }});
    }

    @GetMapping("/getMenuByRestaurantID/{id}")
    public ResponseEntity<ResponseBase> getMenuByRestaurantID(@PathVariable UUID id) {
        ArrayList<FoodItem> food;
        List<Integer> restaurantCategories;
        try {
            food = restaurantService.getFoodItemsByRestaurantID(id);
            restaurantCategories = restaurantService.getCategoriesByRestaurantID(id);
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createOKResponse(new MenuItemsResponse(){{
            foodItems = new ArrayList<>(food.stream().map(i -> new NarrowedFoodItem(){{
                id = i.getId();
                name = i.getName();
                description = i.getDescription();
                price = i.getPrice();
                category = i.getCategory();
                imageLink = i.getImageLink();
            }}).toList());
            categories = restaurantCategories;
        }});
    }

    @GetMapping("/getRestaurantByAdminID/{id}")
    public ResponseEntity<ResponseBase> getRestaurantByAdminID(@PathVariable UUID id) {
        Restaurant restaurant;
        try {
            restaurant = administratorService.getFoodItemsByRestaurantID(id);
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        if (restaurant == null) {
            return createEmptyResponse();
        }
        return createOKResponse(new GetRestaurantByAdminIDResponse(){{
            narrowedRestaurant = new NarrowedRestaurant(){{
                id = restaurant.getId();
                name = restaurant.getName();
                location = restaurant.getLocation();
                deliveryZones = restaurant.getDeliveryZones();
                categories = restaurant.getCategories();
            }};
        }});
    }

    @PostMapping("/addRestaurantToAdmin")
    public ResponseEntity<ResponseBase> addRestaurantToAdmin(@RequestBody AddRestaurantRequest request) {
        Administrator admin;
        try {
            admin = administratorService.tryAddRestaurantToAdmin(request.adminID,
                    request.name, request.location, request.deliveryZones, request.categories);
        } catch (MissingAdministratorException | InvalidRestaurantException ex) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new AddRestaurantResponse(){{
            restaurant = new NarrowedRestaurant(){{
                id = admin.getRestaurant().getId();
                name = admin.getRestaurant().getName();
                location = admin.getRestaurant().getLocation();
                deliveryZones = admin.getRestaurant().getDeliveryZones();
                categories = admin.getRestaurant().getCategories();
            }};
        }});
    }

}
