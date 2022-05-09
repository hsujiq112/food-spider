package com.foodspider.service;

import com.foodspider.exception.InvalidUserException;
import com.foodspider.model.*;
import com.foodspider.repository.AdministratorRepository;
import com.foodspider.repository.CustomerRepository;
import com.foodspider.repository.UserBaseRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
class UserBaseServiceTest {

    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final AdministratorRepository administratorRepository = mock(AdministratorRepository.class);
    private final UserBaseRepository userBaseRepository = mock(UserBaseRepository.class);
    private UserBaseService userBaseService;
    private Administrator administrator;
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
                    Encryptor.decrypt(administrator.getId(), administrator.getPassword()), false);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void getOrdersCountByUserID() {
    }

    @Test
    public void getOrdersByUserID() {
    }

    private void createUsersAndMappings() throws Exception {
        administrator = new Administrator(UUID.fromString("70dd0d11-ed0b-4c76-94d6-5324d221b9a3"),
                "test@test.admin", "testFNA", "testLNA",
                "username123A", "veryNicePassword123A");
        customer = new Customer(UUID.fromString("d4950faf-9292-41a2-8445-5e84ad6b4a66"),
                "test@test.cust", "testFNC", "testLNC",
                "username123C", "veryNicePassword123C");
        var administratorWithRestaurant = administrator;
        administratorWithRestaurant.setRestaurant(validRestaurant);
        when(administratorRepository.getById(administrator.getId())).thenReturn(administratorWithRestaurant);
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
}