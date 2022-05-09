package com.foodspider.service;

import com.foodspider.exception.InvalidRestaurantException;
import com.foodspider.exception.MissingAdministratorException;
import com.foodspider.model.Administrator;
import com.foodspider.model.CategoryEnum;
import com.foodspider.model.Restaurant;
import com.foodspider.repository.AdministratorRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
class AdministratorServiceTest {

    private final AdministratorRepository administratorRepository = mock(AdministratorRepository.class);
    private AdministratorService administratorService;
    private Restaurant validRestaurant;
    private List<Restaurant> invalidRestaurants;
    private Administrator administrator;

    public void setup() throws Exception {
        administratorService = new AdministratorService();
        administratorService.setRepoBase(administratorRepository);
        validRestaurant = new Restaurant("testName", "testLocation", "test,delivery,zones",
                new ArrayList<>(){{
                    add(CategoryEnum.BREAKFAST.ordinal());
                    add(CategoryEnum.LUNCH.ordinal());
                    add(CategoryEnum.DINNER.ordinal());
                }});
        administrator = new Administrator(UUID.fromString("443eaf25-00b4-4aec-800e-118d46f5f527"),
                "test@test.test", "testFN", "testLN",
                "username123", "veryNicePassword123");
        var administratorWithRestaurant = administrator;
        administratorWithRestaurant.setRestaurant(validRestaurant);
        when(administratorRepository.getById(administrator.getId())).thenReturn(administratorWithRestaurant);
        when(administratorRepository.save(administrator)).thenReturn(administratorWithRestaurant);
        createInvalidRestaurants();
    }

    @Test
    public void testTryAddRestaurantToAdmin() {
        try {
            setup();
            var updatedAdmin = administratorService.tryAddRestaurantToAdmin(administrator.getId(), validRestaurant.getName(),
                    validRestaurant.getLocation(), validRestaurant.getDeliveryZones(), validRestaurant.getCategories());
            var updatedAdminToCheck = administrator;
            updatedAdminToCheck.setRestaurant(validRestaurant);
            assertEquals(updatedAdminToCheck, updatedAdmin);
        } catch (Exception ex) {
            fail();
        }

    }

    @Test
    public void testTryAddRestaurantToAdminInvalidRestaurants() {
        try {
            setup();
            var invalidRestaurantsCount = 0;
            for(var restaurant: invalidRestaurants) {
                try {
                    administratorService.tryAddRestaurantToAdmin(UUID.fromString("443eaf25-00b4-4aec-800e-118d46f5f527"),
                            restaurant.getName(), restaurant.getLocation(),
                            restaurant.getDeliveryZones(), restaurant.getCategories());
                } catch (InvalidRestaurantException ex) {
                    invalidRestaurantsCount++;
                }
            }
            assertEquals(invalidRestaurants.size(), invalidRestaurantsCount);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void testTryAddRestaurantToAdminWithoutAdministrator() {
        try {
            setup();
            administratorService.tryAddRestaurantToAdmin(UUID.randomUUID(), validRestaurant.getName(),
                    validRestaurant.getLocation(), validRestaurant.getDeliveryZones(), validRestaurant.getCategories());
        } catch (MissingAdministratorException ex) {
            return;
        } catch (Exception ex) {
            fail();
        }
        fail();
    }

    @Test
    public void testGetFoodItemsByRestaurantID() {
        try {
            setup();
            var restaurant = administratorService.getRestaurantByAdminID(administrator.getId());
            assertEquals(restaurant, validRestaurant);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void testGetFoodItemsByRestaurantIDMissingAdministrator() {
        try {
            setup();
            administratorService.getRestaurantByAdminID(UUID.randomUUID());
        } catch (MissingAdministratorException ex) {
            return;
        } catch (Exception ex) {
            fail();
        }
        fail();
    }

    private void createInvalidRestaurants() {
        invalidRestaurants = new ArrayList<>() {{
            add(new Restaurant("", "testLocation", "test,delivery,zones",
                    new ArrayList<>(){{
                        add(CategoryEnum.BREAKFAST.ordinal());
                        add(CategoryEnum.LUNCH.ordinal());
                        add(CategoryEnum.DINNER.ordinal());
                    }}));
            add(new Restaurant(RandomString.make(257), "testLocation", "test,delivery,zones",
                    new ArrayList<>(){{
                        add(CategoryEnum.BREAKFAST.ordinal());
                        add(CategoryEnum.LUNCH.ordinal());
                        add(CategoryEnum.DINNER.ordinal());
                    }}));
            add(new Restaurant("testName", "", "test,delivery,zones",
                    new ArrayList<>(){{
                        add(CategoryEnum.BREAKFAST.ordinal());
                        add(CategoryEnum.LUNCH.ordinal());
                        add(CategoryEnum.DINNER.ordinal());
                    }}));
            add(new Restaurant("testName", RandomString.make(257), "test,delivery,zones",
                    new ArrayList<>(){{
                        add(CategoryEnum.BREAKFAST.ordinal());
                        add(CategoryEnum.LUNCH.ordinal());
                        add(CategoryEnum.DINNER.ordinal());
                    }}));
            add(new Restaurant("testName", "testLocation", "",
                    new ArrayList<>(){{
                        add(CategoryEnum.BREAKFAST.ordinal());
                        add(CategoryEnum.LUNCH.ordinal());
                        add(CategoryEnum.DINNER.ordinal());
                    }}));
            add(new Restaurant("testName", "testLocation", RandomString.make(257),
                    new ArrayList<>(){{
                        add(CategoryEnum.BREAKFAST.ordinal());
                        add(CategoryEnum.LUNCH.ordinal());
                        add(CategoryEnum.DINNER.ordinal());
                    }}));
            add(new Restaurant("testName", "testLocation", RandomString.make(257),
                    new ArrayList<>()));
        }};
    }
}