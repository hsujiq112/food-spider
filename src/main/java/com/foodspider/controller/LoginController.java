package com.foodspider.controller;

import com.foodspider.exception.InvalidUserException;
import com.foodspider.model.Administrator;
import com.foodspider.model.Customer;
import com.foodspider.model.UserBase;
import com.foodspider.model.request_model.LoginRequest;
import com.foodspider.model.request_model.RegisterRequest;
import com.foodspider.service.CustomerService;
import com.foodspider.service.UserBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    @Qualifier("userService")
    private UserBaseService userService;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        UserBase userBase;
        try {
            userBase = userService.tryLogin(loginRequest.username, loginRequest.password);
        } catch (InvalidUserException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userBase);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest registerRequest) {
        Customer customer = null;
        UserBase administrator = null;
        try {
            if (!registerRequest.isAdmin) {
                customer = customerService.tryRegister(registerRequest.emailAddress, registerRequest.firstName,
                        registerRequest.lastName, registerRequest.username, registerRequest.password);
            } else {
                administrator = userService.tryRegister(registerRequest.emailAddress, registerRequest.firstName,
                        registerRequest.lastName, registerRequest.username, registerRequest.password);
            }
        } catch (InvalidUserException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(registerRequest.isAdmin ? administrator : customer);
    }
}
