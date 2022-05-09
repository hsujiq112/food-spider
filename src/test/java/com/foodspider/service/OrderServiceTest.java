package com.foodspider.service;

import com.foodspider.exception.InvalidOrderChangeException;
import com.foodspider.exception.MissingCustomerException;
import com.foodspider.exception.MissingFoodItemException;
import com.foodspider.model.*;
import com.foodspider.model.request_model.AddOrderRequest;
import com.foodspider.repository.CustomerRepository;
import com.foodspider.repository.FoodItemRepository;
import com.foodspider.repository.OrderRepository;
import com.foodspider.repository.RestaurantRepository;
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
class OrderServiceTest {

    private final FoodItemRepository foodItemRepository = mock(FoodItemRepository.class);
    private final RestaurantRepository restaurantRepository = mock(RestaurantRepository.class);
    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private Customer customer;
    private Restaurant restaurant;
    private OrderService orderService;
    private ArrayList<FoodItem> foodItemsList;

    public void setup() throws Exception {
        orderService = new OrderService();
        orderService.setRepoBase(orderRepository);
        var customerService = new CustomerService();
        customerService.setRepoBase(customerRepository);
        var foodItemService = new FoodItemService();
        foodItemService.setRepoBase(foodItemRepository);
        var restaurantService = new RestaurantService();
        restaurantService.setRepoBase(restaurantRepository);
        var mailService = new MailService();
        mailService.setFoodItemService(foodItemService);

        orderService.setCustomerService(customerService);
        orderService.setFoodItemService(foodItemService);
        orderService.setRestaurantService(restaurantService);
        orderService.setMailService(mailService);

        customer = new Customer(UUID.fromString("1c9dc9ee-759b-4db7-88e1-f339afeb06e4"), "test@test.email",
                "testFn", "testLn", "testUsername", "password123!");
        when(customerRepository.getById(customer.getId())).thenReturn(customer);
        restaurant = new Restaurant(UUID.fromString("2217826a-490b-4ab2-aab3-1908b1b3c680"), "testName",
                "testLocation", "test,delivery,zones", new ArrayList<>(){{
                    add(CategoryEnum.BREAKFAST.ordinal());
                    add(CategoryEnum.LUNCH.ordinal());
                    add(CategoryEnum.DINNER.ordinal());
                }});
        var administrator = new Administrator(UUID.fromString("70dd0d11-ed0b-4c76-94d6-5324d221b9a3"),
                "test@test.admin", "testFNA", "testLNA",
                "username123A", "veryNicePassword123A");
        restaurant.setAdministrator(administrator);
        when(restaurantRepository.getById(restaurant.getId())).thenReturn(restaurant);
        createFoodItemsAndMappings();
        createOrdersAndMappings();
    }

    @Test
    void tryAddOrderInvalidUser() {
        try {
            setup();
            var addOrderRequest = mock(AddOrderRequest.class);
            addOrderRequest.userId = UUID.randomUUID();
            orderService.tryAddOrder(addOrderRequest);
        } catch (MissingCustomerException ex) {
            return;
        } catch (Exception ex) {
            fail();
        }
        fail();
    }

    @Test
    void tryAddOrderInvalidFoodItems() {
        try {
            setup();
            var addOrderRequest = new AddOrderRequest(){{
                userId = customer.getId();
                foodItems = new ArrayList<>();
            }};
            orderService.tryAddOrder(addOrderRequest);
        } catch (MissingFoodItemException ex) {
            return;
        } catch (Exception ex) {
            fail();
        }
        fail();
    }

    @Test
    void tryAddOrder() {
        try {
            setup();
            var addOrderRequest = new AddOrderRequest(){{
                foodItems = foodItemsList.stream().map(BaseModel::getId).toList();
                userId = customer.getId();
                restaurantId = restaurant.getId();
                address = "testAddress";
                specialDetails = "testSpecialDetails";
            }};
            orderService.tryAddOrder(addOrderRequest);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void changeStatusToOrder() {
        try {
            setup();
            orderService.changeStatusToOrder(UUID.fromString("86976fef-b413-4d6f-86e9-4f97403c62b6"), 2);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void changeStatusToOrderInvalid() {
        var exceptionCount = 0;
        try {
            setup();
            orderService.changeStatusToOrder(UUID.fromString("4d5e02d6-ce78-4519-b6f9-ff495e2c3270"), 2);
        } catch (InvalidOrderChangeException ex) {
            exceptionCount++;
        } catch (Exception ex) {
            fail();
        }
        try {
            orderService.changeStatusToOrder(UUID.fromString("96951a02-c265-479b-9ac1-b9b9d3ad24a3"), 2);
        } catch (InvalidOrderChangeException ex) {
            exceptionCount++;
        }
        assertEquals(exceptionCount, 2);
    }

    private void createFoodItemsAndMappings() {
        var foodItem1 = new FoodItem(UUID.fromString("28485f24-31d0-4d18-a95f-ca3580d11771"),
                "testName", "testDesc", 101.0, CategoryEnum.BREAKFAST, "testImage");
        var foodItem2 = new FoodItem(UUID.fromString("00363280-bedf-4c48-a0aa-7b246314f7c3"),
                "testName2", "testDesc2", 102.0, CategoryEnum.LUNCH, "testImage2");
        var foodItem3 = new FoodItem(UUID.fromString("411a1e07-7286-41ff-9a20-693c6c100a21"),
                "testName3", "testDesc3", 103.0, CategoryEnum.DINNER, "testImage3");
        foodItemsList = new ArrayList<>(){{
            add(foodItem1);
            add(foodItem2);
            add(foodItem3);
        }};
        when(foodItemRepository.getById(foodItem1.getId())).thenReturn(foodItem1);
        when(foodItemRepository.getById(foodItem2.getId())).thenReturn(foodItem2);
        when(foodItemRepository.getById(foodItem3.getId())).thenReturn(foodItem3);
    }

    private void createOrdersAndMappings() {
        var order1 = new Order(UUID.fromString("4d5e02d6-ce78-4519-b6f9-ff495e2c3270"), restaurant,
                customer, foodItemsList.subList(0, 1));
        var order2 = new Order(UUID.fromString("96951a02-c265-479b-9ac1-b9b9d3ad24a3"), restaurant,
                customer, foodItemsList.subList(0, 0));
        var order3 = new Order(UUID.fromString("86976fef-b413-4d6f-86e9-4f97403c62b6"), restaurant,
                customer, foodItemsList);
        order1.setOrderStatus(OrderStatusEnum.DECLINED);
        order2.setOrderStatus(OrderStatusEnum.DELIVERED);
        when(orderRepository.getById(order1.getId())).thenReturn(order1);
        when(orderRepository.getById(order2.getId())).thenReturn(order2);
        when(orderRepository.getById(order3.getId())).thenReturn(order3);
    }
}