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

    @Mock
    private final RepositoryBase<Administrator> administratorRepositoryBase = mock(AdministratorRepository.class);
    @Mock
    private final UserBaseService userBaseService = mock(UserBaseService.class);

    @Test
    void test() throws Exception {
        when(userBaseService.getByUsername(mock(UserBase.class).getUsername())).thenReturn(null);
        var userToAdd = mock(UserBase.class);
        when(userBaseService.tryRegister(userToAdd.getEmailAddress(), userToAdd.getFirstName(), userToAdd.getLastName(),
                userToAdd.getUsername(), userToAdd.getPassword(), false)).thenThrow(InvalidUserException.class);
        when(userBaseService.tryRegister("test@test.test", "firstName", "lastName",
                "validUsername", "Password123!", false)).thenReturn(new Customer());
        MatcherAssert.assertThat(userBaseService, isNull());
    }


    @Test
    void CRUDOperations() {
        try {

        } catch (Exception ex) {
            //oeuf
        }
    }

    @Test
    void tryAddRestaurantToAdmin() {

    }

    @Test
    void getFoodItemsByRestaurantID() {

    }
}