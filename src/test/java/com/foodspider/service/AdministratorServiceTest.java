package com.foodspider.service;

import com.foodspider.exception.InvalidUserException;
import com.foodspider.model.Administrator;
import com.foodspider.model.Customer;
import com.foodspider.model.UserBase;
import com.foodspider.repository.AdministratorRepository;
import com.foodspider.repository.RepositoryBase;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class AdministratorServiceTest {

    @Test
    void testTryAddRestaurantToAdmin() {
    }

    @Test
    void testGetFoodItemsByRestaurantID() {
    }
}