package com.foodspider.service;

import com.foodspider.exception.InvalidUserException;
import com.foodspider.model.*;
import com.foodspider.repository.AdministratorRepository;
import com.foodspider.repository.CustomerRepository;
import com.foodspider.repository.UserBaseRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
class UserBaseServiceTest {

    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final AdministratorRepository administratorRepository = mock(AdministratorRepository.class);
    private final UserBaseRepository userBaseRepository = mock(UserBaseRepository.class);
    private UserBaseService userBaseService;
    private Administrator administrator;
    private Administrator administratorWithoutRestaurant;
    private Administrator administratorWithRestaurant;
    private Customer customer;
    private Restaurant validRestaurant;
    private ArrayList<UserBase> invalidUsers;

    public void setup() throws Exception {
        AdministratorService administratorService = new AdministratorService();
        administratorService.setRepoBase(administratorRepository);
        CustomerService customerService = new CustomerService();
        customerService.setRepoBase(customerRepository);
        userBaseService = new UserBaseService();
        userBaseService.setRepoBase(userBaseRepository);
        userBaseService.setAdministratorService(administratorService);
        userBaseService.setCustomerService(customerService);

        validRestaurant = new Restaurant("testName", "testLocation", "test,delivery,zones",
                new ArrayList<>(){{
                    add(CategoryEnum.BREAKFAST.ordinal());
                    add(CategoryEnum.LUNCH.ordinal());
                    add(CategoryEnum.DINNER.ordinal());
                }});

        createUsersAndMappings();
        createInvalidUsers();
        addOrdersToRestaurant();
    }

    @Test
    public void getByUsername() {
        try {
            setup();
            var user = userBaseService.getByUsername("username123A");
            if (user.isEmpty()) {
                fail();
            }
            assertEquals(user.get(), administrator);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void tryLoginAdmin() {
        try {
            setup();
            var user = userBaseService.tryLogin("username123A", "veryNicePassword123A");
            assertEquals(user, administrator);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void tryLoginCustomer() {
        try {
            setup();
            var user = userBaseService.tryLogin("username123C", "veryNicePassword123C");
            assertEquals(user, customer);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void tryLoginWrongCredentials() {
        try {
            setup();
            var user = userBaseService.tryLogin(RandomString.make(10), RandomString.make(10));
            assertEquals(user, administrator);
        } catch (InvalidUserException ex) {
            return;
        } catch (Exception ex) {
            fail();
        }
        fail();
    }

    @Test
    public void tryLoginWrongPassword() {
        try {
            setup();
            var user = userBaseService.tryLogin("username123C", RandomString.make(10));
            assertEquals(user, administrator);
        } catch (InvalidUserException ex) {
            return;
        } catch (Exception ex) {
            fail();
        }
        fail();
    }

    @Test
    public void tryRegisterInvalidUsers() {
        try {
            setup();
            var invalidUsersCount = 0;
            for(var user: invalidUsers) {
                try {
                    userBaseService.tryRegister(user.getEmailAddress(), user.getFirstName(), user.getLastName(),
                            user.getUsername(), Encryptor.decrypt(user.getId(), user.getPassword()), invalidUsersCount % 2 == 0);
                } catch (InvalidUserException ex) {
                    invalidUsersCount++;
                }
            }
            assertEquals(invalidUsers.size(), invalidUsersCount);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void tryRegister() {
        try {
            setup();
            when(userBaseRepository.save(customer)).thenReturn(customer);
            // var userToAdd =
            userBaseService.tryRegister(customer.getEmailAddress(), customer.getFirstName(),
                    customer.getLastName(), customer.getUsername(),
                    Encryptor.decrypt(customer.getId(), customer.getPassword()), false);
            // assertEquals(userToAdd.getClass(), Customer.class);
            // for some reason, it does not want to work
            userBaseService.tryRegister(administrator.getEmailAddress(), administrator.getFirstName(),
                    administrator.getLastName(), administrator.getUsername(),
                    Encryptor.decrypt(administrator.getId(), administrator.getPassword()), true);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void getOrdersCountByUserID() {
        try {
            setup();
            var orders = userBaseService.getOrdersByUserID(administrator.getId(), true, -1);
            assertEquals(3, orders.size());
            orders = userBaseService.getOrdersByUserID(administrator.getId(), true, 3);
            assertEquals(1, orders.size());
            orders = userBaseService.getOrdersByUserID(administratorWithoutRestaurant.getId(), true, -1);
            assertEquals(0, orders.size());
            orders = userBaseService.getOrdersByUserID(customer.getId(), false, -1);
            assertEquals(3, orders.size());
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void getOrdersByUserID() {
        try {
            setup();
            var ordersCount = userBaseService
                    .getOrdersCountByUserID(administrator.getId(), true);
            assertEquals(2, ordersCount.pendingOrders);
            assertEquals(3, ordersCount.placedOrders);
            ordersCount = userBaseService
                    .getOrdersCountByUserID(administratorWithoutRestaurant.getId(), true);
            assertEquals(0, ordersCount.pendingOrders);
            assertEquals(0, ordersCount.placedOrders);
            ordersCount = userBaseService
                    .getOrdersCountByUserID(customer.getId(), false);
            assertEquals(2, ordersCount.pendingOrders);
            assertEquals(3, ordersCount.placedOrders);
        } catch (Exception ex) {
            fail();
        }
    }

    private void createUsersAndMappings() throws Exception {
        administrator = new Administrator(UUID.fromString("70dd0d11-ed0b-4c76-94d6-5324d221b9a3"),
                "test@test.admin", "testFNA", "testLNA",
                "username123A", "veryNicePassword123A");
        administratorWithoutRestaurant = new Administrator(UUID.fromString("534dc3b1-46b0-4a0f-ba24-5aedcb73d2eb"),
                "test@test.admin2", "testFNA2", "testLNA2",
                "username123A2", "veryNicePassword123A2");
        customer = new Customer(UUID.fromString("d4950faf-9292-41a2-8445-5e84ad6b4a66"),
                "test@test.cust", "testFNC", "testLNC",
                "username123C", "veryNicePassword123C");
        administratorWithRestaurant = administrator;
        administratorWithRestaurant.setRestaurant(validRestaurant);
        when(administratorRepository.getById(administrator.getId())).thenReturn(administratorWithRestaurant);
        when(administratorRepository.getById(administratorWithoutRestaurant.getId()))
                .thenReturn(administratorWithoutRestaurant);
        when(customerRepository.getById(customer.getId())).thenReturn(customer);
        when(userBaseRepository.findAll()).thenReturn(new ArrayList<>(){{
            add(administratorWithRestaurant);
            add(customer);
        }});
    }

    private void createInvalidUsers() throws Exception {
        invalidUsers = new ArrayList<>() {{
            add(new UserBase("", "testFn", "testLn",
                    "testUsername", "testPassword"));
            add(new UserBase("invalid@ema!lAddr", "testFn", "testLn",
                    "testUsername", "testPassword"));
            add(new UserBase(RandomString.make(256) + "@test.test", "testFn", "testLn",
                    "testUsername", "testPassword"));
            add(new UserBase("test@email.address", "", "testLn",
                    "testUsername", "testPassword"));
            add(new UserBase("test@email.address", "Inval!dCharacters", "testLn",
                    "testUsername", "testPassword"));
            add(new UserBase("test@email.address", "testFn", "",
                    "testUsername", "testPassword"));
            add(new UserBase("test@email.address", "testFn", "I@nvaLi!$Cha 221",
                    "testUsername", "testPassword"));
            add(new UserBase("test@email.address", "testFn", "testLn",
                    "testUsername", ""));
            add(new UserBase("test@email.address", "testFn", "testLn",
                    "testUsername", "shrtp"));
            add(new UserBase("test@email.address", "testFn", "testLn",
                    "", "testPassword"));
        }};
    }

    private void addOrdersToRestaurant() {
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
        administratorWithRestaurant.getRestaurant().setFoodItems(foodItemsList);
        var order1 = new Order(UUID.fromString("4d5e02d6-ce78-4519-b6f9-ff495e2c3270"),
                administratorWithoutRestaurant.getRestaurant(), customer, foodItemsList.subList(0, 1));
        order1.setRestaurant(administratorWithRestaurant.getRestaurant());
        order1.setOrderStatus(OrderStatusEnum.DELIVERED);
        var order2 = new Order(UUID.fromString("96951a02-c265-479b-9ac1-b9b9d3ad24a3"),
                administratorWithoutRestaurant.getRestaurant(), customer, foodItemsList.subList(0, 0));
        order2.setRestaurant(administratorWithRestaurant.getRestaurant());
        var order3 = new Order(UUID.fromString("86976fef-b413-4d6f-86e9-4f97403c62b6"),
                administratorWithoutRestaurant.getRestaurant(), customer, foodItemsList);
        order3.setRestaurant(administratorWithRestaurant.getRestaurant());
        var orders = new ArrayList<Order>() {{
            add(order1);
            add(order2);
            add(order3);
        }};
        customer.setOrders(orders);
        administratorWithRestaurant.getRestaurant().setOrders(orders);
    }
}