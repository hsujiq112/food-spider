package com.foodspider;

import com.foodspider.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class FoodSpiderApplication {

    @Bean(name = "uService")
    public UserService userService() {
        return new UserService();
    }

    public static void main(String[] args) {
        SpringApplication.run(FoodSpiderApplication.class, args);
    }

}
