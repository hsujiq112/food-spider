package com.foodspider;

import com.foodspider.service.CustomerService;
import com.foodspider.service.UserBaseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class FoodSpiderApplication {

    @Bean(name = "userService")
    public UserBaseService userService() {
        return new UserBaseService();
    }

    @Bean(name = "customerService")
    public CustomerService customerService() {
        return new CustomerService();
    }

    public static void main(String[] args) {
        SpringApplication.run(FoodSpiderApplication.class, args);
    }

}
