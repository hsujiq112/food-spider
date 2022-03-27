package com.foodspider.controller;

import com.foodspider.exception.InvalidRestaurantException;
import com.foodspider.exception.MissingAdministratorException;
import com.foodspider.model.Administrator;
import com.foodspider.model.FoodItem;
import com.foodspider.model.narrowed_model.NarrowedFoodItem;
import com.foodspider.model.narrowed_model.NarrowedRestaurant;
import com.foodspider.model.request_model.AddFoodToCategoryRequest;
import com.foodspider.model.request_model.AddRestaurantRequest;
import com.foodspider.model.response_model.*;
import com.foodspider.service.AdministratorService;
import com.foodspider.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@Controller
public class RestaurantController extends ControllerBase {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private AdministratorService administratorService;

    @GetMapping("/restaurants")
    public ResponseEntity<ResponseBase> getRestaurants() {
        ArrayList<NarrowedRestaurant> restaurantsList;
        try {
            restaurantsList = new ArrayList<>(restaurantService.dbSet().stream().map(i -> new NarrowedRestaurant(){{
                name = i.getName();
                location = i.getLocation();
                deliveryZones = i.getDeliveryZones();
            }}).toList());
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createOKResponse(new GetRestaurantsResponse(){{
            restaurants = restaurantsList;
        }});
    }

    @GetMapping("/getMenuByRestaurantID/{id}")
    public ResponseEntity<ResponseBase> getByRestaurantID(@RequestParam UUID id) {
        ArrayList<FoodItem> food;
        try {
            food = restaurantService.getFoodItemsByRestaurantID(id);
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createOKResponse(new MenuItemsResponse(){{
            foodItems = new ArrayList<>(food.stream().map(i -> new NarrowedFoodItem(){{
                name = i.getName();
                description = i.getDescription();
                price = i.getPrice();
                category = i.getCategory();
                imageLink = i.getImageLink();
            }}).toList());
        }});
    }

    @PostMapping("/addRestaurantToAdmin")
    public ResponseEntity<ResponseBase> addRestaurantToAdmin(@RequestBody AddRestaurantRequest request) {
        Administrator admin;
        try {
            admin = administratorService.tryAddRestaurantToAdmin(request.adminID,
                    request.name, request.location, request.deliveryZones);
        } catch (MissingAdministratorException | InvalidRestaurantException ex) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new AddRestaurantResponse(){{
            administrator = admin;
        }});
    }

    @PatchMapping("/addFoodToCategory")
    public ResponseEntity<EmptyResponse> addFoodToCategory(@RequestBody AddFoodToCategoryRequest request) {
        try {
            restaurantService.addFoodToRestaurant(request.restaurantID, request.foodName, request.foodDescription,
                    request.price, request.categoryEnum, request.foodImageLink);
        } catch (Exception ex) {
            createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createEmptyResponse();
    }

}
