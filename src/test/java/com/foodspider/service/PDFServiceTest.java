package com.foodspider.service;

import com.foodspider.model.CategoryEnum;
import com.foodspider.model.FoodItem;
import com.foodspider.model.Restaurant;
import com.foodspider.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class PDFServiceTest {

    private PDFService pdfService;
    private final RestaurantRepository restaurantRepository = mock(RestaurantRepository.class);
    private Restaurant restaurant;

    public void setup() {
        RestaurantService restaurantService = new RestaurantService();
        restaurantService.setRepoBase(restaurantRepository);
        pdfService = new PDFService();
        pdfService.setRestaurantService(restaurantService);
        var categories = new ArrayList<Integer>(){{
            add(1);
        }};
        restaurant  = new Restaurant(UUID.fromString("134e0875-10e1-478b-94e8-062bcab0b38f"),
                "testName", "testLocation", "test,delivery,zones", categories);
        addFoodItemsToRestaurant();
        when(restaurantRepository.getById(restaurant.getId())).thenReturn(restaurant);
    }

    @Test
    void createPDF() {
        setup();
        try {
            var pdf = pdfService.createPDF(restaurant.getId());
            assertEquals("testName", pdf.getValue());
            assertTrue(pdf.getKey().size() > 0);
        } catch (Exception ex) {
            fail();
        }
    }

    private void addFoodItemsToRestaurant() {
        var foodItem1 = new FoodItem(UUID.fromString("28485f24-31d0-4d18-a95f-ca3580d11771"),
                "testName", "testDesc", 101.0, CategoryEnum.BREAKFAST, "testImage");
        var foodItem2 = new FoodItem(UUID.fromString("00363280-bedf-4c48-a0aa-7b246314f7c3"),
                "testName2", "testDesc2", 102.0, CategoryEnum.LUNCH, "testImage2");
        var foodItem3 = new FoodItem(UUID.fromString("411a1e07-7286-41ff-9a20-693c6c100a21"),
                "testName3", "testDesc3", 103.0, CategoryEnum.DINNER, "testImage3");
        var foodItemsList = new ArrayList<FoodItem>(){{
            add(foodItem1);
            add(foodItem2);
            add(foodItem3);
        }};
        restaurant.setFoodItems(foodItemsList);
    }
}