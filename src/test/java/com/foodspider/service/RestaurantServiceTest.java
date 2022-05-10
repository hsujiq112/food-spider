package com.foodspider.service;

import com.foodspider.exception.InvalidFoodItemException;
import com.foodspider.model.CategoryEnum;
import com.foodspider.model.FoodItem;
import com.foodspider.model.Restaurant;
import com.foodspider.repository.RestaurantRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
class RestaurantServiceTest {

    private final RestaurantRepository restaurantRepository = mock(RestaurantRepository.class);
    private ArrayList<FoodItem> invalidFoodItems;
    private RestaurantService restaurantService;
    private Restaurant restaurant;
    private ArrayList<Restaurant> restaurants;

    public void setup() {
        restaurantService = new RestaurantService();
        restaurantService.setRepoBase(restaurantRepository);

        var categories = new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(3);
            add(4);
        }};
        restaurant = new Restaurant(UUID.fromString("134e0875-10e1-478b-94e8-062bcab0b38f"),
                "testName", "testLocation", "test,delivery,zones", categories);
        when(restaurantRepository.getById(restaurant.getId())).thenReturn(restaurant);
        createInvalidFoodItems();
        addFoodItemsToRestaurant();
        createAndMapRestaurants();
    }

    @Test
    void getFoodItemsByRestaurantID() {
        setup();
        var foodItems = restaurantService.getFoodItemsByRestaurantID(restaurant.getId());
        assertEquals(3, foodItems.size());
    }

    @Test
    void getCategoriesByRestaurantID() {
        setup();
        var categories = restaurantService.getCategoriesByRestaurantID(restaurant.getId());
        assertEquals(4, categories.size());
    }

    @Test
    void addFoodToRestaurantInvalidFoodItems() {
        setup();
        var invalidFoodItemsCount = 0;
        for(var foodItem: invalidFoodItems) {
            try {
                restaurantService.addFoodToRestaurant(restaurant.getId(), foodItem.getName(), foodItem.getDescription(),
                        foodItem.getPrice(), foodItem.getCategory(), foodItem.getImageLink());
            } catch (InvalidFoodItemException ex) {
                invalidFoodItemsCount++;
            }
        }
        assertEquals(invalidFoodItems.size(), invalidFoodItemsCount);
    }

    @Test
    void addFoodToRestaurant() {
        setup();
        try {
            restaurantService.addFoodToRestaurant(restaurant.getId(), "testName",
                    "testDescription", 200.0,
                    CategoryEnum.BREAKFAST, "https://test.url");
        } catch (Exception ex) {
            fail();
        }

    }

    private void createInvalidFoodItems() {
        invalidFoodItems = new ArrayList<>(){{
            add(new FoodItem("", "testDescription", 100.0, CategoryEnum.BREAKFAST,
                    "https://www.kfc.ro/newkfc/img/hpbox-bucket.jpg"));
            add(new FoodItem(RandomString.make(257), "testDescription", 100.0, CategoryEnum.BREAKFAST,
                    "https://www.kfc.ro/newkfc/img/hpbox-bucket.jpg"));
            add(new FoodItem("testName", "", 100.0, CategoryEnum.BREAKFAST,
                    "https://www.kfc.ro/newkfc/img/hpbox-bucket.jpg"));
            add(new FoodItem("testName", RandomString.make(257), 100.0, CategoryEnum.BREAKFAST,
                    "https://www.kfc.ro/newkfc/img/hpbox-bucket.jpg"));
            add(new FoodItem("testName", "testDescription", -10.0, CategoryEnum.BREAKFAST,
                    "https://www.kfc.ro/newkfc/img/hpbox-bucket.jpg"));
            add(new FoodItem("testName", "testDescription", 100.0, null,
                    "https://www.kfc.ro/newkfc/img/hpbox-bucket.jpg"));
            add(new FoodItem("testName", "testDescription", 100.0, CategoryEnum.BREAKFAST,
                    ""));
            add(new FoodItem("testName", "testDescription", 100.0, CategoryEnum.BREAKFAST,
                    "https://invalidURL"));
            add(new FoodItem("testName", "testDescription", 100.0, CategoryEnum.BREAKFAST,
                    "https://" + RandomString.make(62) + "." + RandomString.make(200)));
        }};
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

    private void createAndMapRestaurants() {

    }
}