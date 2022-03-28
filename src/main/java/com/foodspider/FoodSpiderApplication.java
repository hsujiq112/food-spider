package com.foodspider;

import com.foodspider.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@SpringBootApplication
public class FoodSpiderApplication implements WebMvcConfigurer{

    @Bean(name = "userService")
    public UserBaseService userService() {
        return new UserBaseService();
    }
    @Bean(name = "cService")
    public CustomerService customerService() {
        return new CustomerService();
    }
    @Bean(name = "aService")
    public AdministratorService administratorService() {
        return new AdministratorService();
    }
    @Bean(name = "fiService")
    public FoodItemService foodItemService() {
        return new FoodItemService();
    }
    @Bean(name = "oService")
    public OrderService orderService() {
        return new OrderService();
    }
    @Bean(name = "rService")
    public RestaurantService restaurantService() {
        return new RestaurantService();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    public static void main(String[] args) {
        SpringApplication.run(FoodSpiderApplication.class, args);
    }

}
