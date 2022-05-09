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
import com.foodspider.service.PDFService;
import com.foodspider.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class RestaurantController extends ControllerBase {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private PDFService pdfService;

    @GetMapping("/restaurants")
    public ResponseEntity<ResponseBase> getRestaurants() {
        ArrayList<NarrowedRestaurant> restaurantsList;
        try {
            LOGGER.debug("Getting all restaurants...");
            restaurantsList = restaurantService.getNarrowedRestaurants(null);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        LOGGER.info("Successfully got all restaurants; count: " + restaurantsList.size());
        return createOKResponse(new GetRestaurantsResponse(){{
            restaurants = restaurantsList;
        }});
    }

    @GetMapping("/restaurants/{filter}")
    public ResponseEntity<ResponseBase> getRestaurants(@PathVariable String filter) {
        ArrayList<NarrowedRestaurant> restaurantsList;
        try {
            LOGGER.debug("getting all restaurants filtered... filter: " + filter);
            restaurantsList = restaurantService.getNarrowedRestaurants(filter);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        LOGGER.info("Successfully got all restaurants filtered with filter " + filter);
        return createOKResponse(new GetRestaurantsResponse(){{
            restaurants = restaurantsList;
        }});
    }

    @GetMapping("/getMenuByRestaurantID/{id}")
    public ResponseEntity<ResponseBase> getMenuByRestaurantID(@PathVariable UUID id) {
        ArrayList<FoodItem> food;
        List<Integer> restaurantCategories;
        try {
            LOGGER.debug("Getting whole menu for restaurant... restaurantId: " + id);
            food = restaurantService.getFoodItemsByRestaurantID(id);
            restaurantCategories = restaurantService.getCategoriesByRestaurantID(id);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        LOGGER.info("Successfully got the menu for restaurantId: " + id);
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
            LOGGER.debug("Getting admin's restaurant... adminId: " + id);
            restaurant = administratorService.getRestaurantByAdminID(id);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        if (restaurant == null) {
            return createEmptyResponse();
        }
        LOGGER.info("Successfully got admin's restaurant... adminId: " + id);
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
            LOGGER.debug("Adding restaurant to admin... " + request.name);
            admin = administratorService.tryAddRestaurantToAdmin(request.adminID,
                    request.name, request.location, request.deliveryZones, request.categories);
        } catch (MissingAdministratorException | InvalidRestaurantException ex) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        LOGGER.info("Successfully added the restaurant to admin; adminId: " + request.adminID
                + " , restaurant name: " + request.name);
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

    @GetMapping("/exportAsPDF/{restaurantId}")
    public ResponseEntity<byte[]> exportAsPDF(@PathVariable UUID restaurantId) {
        byte[] pdfBinary;
        Map.Entry<ByteArrayOutputStream, String> pdf;
        try {
            LOGGER.debug("creating pdf... restaurantId: " + restaurantId );
            pdf = pdfService.createPDF(restaurantId);
            pdfBinary = pdf.getKey().toByteArray();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
        LOGGER.info("Successfully created pdf for restaurant " + restaurantId);
        return createBinaryResponse(pdfBinary, "application/pdf", pdf.getValue() + ".pdf");
    }

}
