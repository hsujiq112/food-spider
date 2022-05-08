package com.foodspider.controller;

import com.foodspider.exception.FoodItemInUseException;
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
            LOGGER.debug("Adding food to restaurant");
            restaurantService.addFoodToRestaurant(request.restaurantID, request.foodName, request.foodDescription,
                    request.price, request.categoryEnum, request.foodImageLink);
        } catch (InvalidFoodItemException ex) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        LOGGER.info("Successfully added the food to restaurant: " + request.foodName);
        return createEmptyResponse();
    }

    @DeleteMapping("/foodItem/{id}")
    public ResponseEntity<ResponseBase> delete(@PathVariable UUID id) {
        try {
            LOGGER.debug("Deleting food by id " + id);
            foodItemService.deleteByID(id);
        } catch (MissingFoodItemException | FoodItemInUseException ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        LOGGER.info("Successfully deleted from restaurant foodItem with id " + id);
        return createEmptyResponse();
    }
}
