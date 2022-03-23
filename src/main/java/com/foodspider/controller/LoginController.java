package com.foodspider.controller;

import com.foodspider.exception.InvalidUserException;
import com.foodspider.model.User;
import com.foodspider.model.request_model.LoginRequest;
import com.foodspider.model.request_model.RegisterRequest;
import com.foodspider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        User user;
        try {
            user = userService.tryLogin(loginRequest.username, loginRequest.password);
        } catch (InvalidUserException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest registerRequest) {
        User user;
        try {
            user = userService.tryRegister(registerRequest.emailAddress, registerRequest.firstName,
                    registerRequest.lastName, registerRequest.username, registerRequest.password);
        } catch (InvalidUserException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
