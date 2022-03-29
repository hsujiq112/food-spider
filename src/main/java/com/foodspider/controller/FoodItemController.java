package com.foodspider.controller;

import com.foodspider.exception.FoodItemInUseExcepiton;
import com.foodspider.exception.InvalidFoodItemException;
import com.foodspider.exception.MissingFoodItemException;
import com.foodspider.model.request_model.AddFoodToCategoryRequest;
import com.foodspider.model.response_model.ResponseBase;
import com.foodspider.service.FoodItemService;
import com.foodspider.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class FoodItemController extends ControllerBase {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private FoodItemService foodItemService;

    @PostMapping("/addFoodToCategory")
    public ResponseEntity<ResponseBase> addFoodToCategory(@RequestBody AddFoodToCategoryRequest request) {
        try {
            restaurantService.addFoodToRestaurant(request.restaurantID, request.foodName, request.foodDescription,
                    request.price, request.categoryEnum, request.foodImageLink);
        } catch (InvalidFoodItemException ex) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createEmptyResponse();
    }

    @DeleteMapping("/foodItem/{id}")
    public ResponseEntity<ResponseBase> delete(@PathVariable UUID id) {
        try {
            foodItemService.deleteByID(id);
        } catch (MissingFoodItemException | FoodItemInUseExcepiton ex) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createEmptyResponse();
    }
}
