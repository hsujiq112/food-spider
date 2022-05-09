package com.foodspider.service;

import com.foodspider.exception.FoodItemInUseException;
import com.foodspider.exception.MissingFoodItemException;
import com.foodspider.model.CategoryEnum;
import com.foodspider.model.FoodItem;
import com.foodspider.model.Order;
import com.foodspider.model.OrderStatusEnum;
import com.foodspider.repository.FoodItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class FoodItemServiceTest {

    private final FoodItemRepository foodItemRepository = mock(FoodItemRepository.class);
    private FoodItemService foodItemService;

    public void setup() {
        foodItemService = new FoodItemService();
        foodItemService.setRepoBase(foodItemRepository);
        var foodItem = new FoodItem(UUID.fromString("6186bd88-cf9b-11ec-9d64-0242ac120002"), "testName",
                "testDesc", 100.0, CategoryEnum.BREAKFAST, "testImage");
        foodItem.setOrders(new ArrayList<>(){{
            add(new Order(OrderStatusEnum.DELIVERED));
        }});
        var foodItemWithDeliveringOrder = new FoodItem(UUID.fromString("9e678d86-cf9b-11ec-9d64-0242ac120002"),
                "testName2", "testDesc2", 101.0, CategoryEnum.BREAKFAST, "testImage2");
        foodItemWithDeliveringOrder.setOrders(new ArrayList<>(){{
            add(new Order(OrderStatusEnum.IN_DELIVERY));
        }});
        when(foodItemRepository.getById(foodItem.getId())).thenReturn(foodItem);
        when(foodItemRepository.getById(foodItemWithDeliveringOrder.getId())).thenReturn(foodItemWithDeliveringOrder);
    }

    @Test
    public void deleteByID() {
        setup();
        try {
            foodItemService.deleteByID(UUID.fromString("6186bd88-cf9b-11ec-9d64-0242ac120002"));
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void deleteByIDFoodItemInUser() {
        setup();
        try {
            foodItemService.deleteByID(UUID.fromString("9e678d86-cf9b-11ec-9d64-0242ac120002"));
        } catch (FoodItemInUseException ex) {
            return;
        } catch (Exception ex) {
            fail();
        }
        fail();
    }

    @Test
    public void deleteByIDMissingFoodItem() {
        setup();
        try {
            foodItemService.deleteByID(UUID.randomUUID());
        } catch (MissingFoodItemException ex) {
            return;
        } catch (Exception ex) {
            fail();
        }
        fail();
    }
}