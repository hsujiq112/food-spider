package com.foodspider.controller;

import com.foodspider.exception.InvalidUserException;
import com.foodspider.model.Administrator;
import com.foodspider.model.Customer;
import com.foodspider.model.UserBase;
import com.foodspider.model.request_model.LoginRequest;
import com.foodspider.model.request_model.RegisterRequest;
import com.foodspider.model.response_model.LoginResponse;
import com.foodspider.model.response_model.RegisterResponse;
import com.foodspider.service.AdministratorService;
import com.foodspider.service.CustomerService;
import com.foodspider.service.UserBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserBaseService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        UserBase user;
        try {
            user = userService.tryLogin(loginRequest.username, loginRequest.password);
        } catch (InvalidUserException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(){{
                isError = true;
                errorMessage = ex.getMessage();
            }});
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponse(){{
                isError = true;
                errorMessage = ex.getMessage();
            }});
        }
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(){{
            isAdmin = user instanceof Administrator;
            userBase = user;
        }});
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.tryRegister(registerRequest.emailAddress, registerRequest.firstName,
                    registerRequest.lastName, registerRequest.username, registerRequest.password, registerRequest.isAdmin);
        } catch (InvalidUserException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponse(){{
                isError = true;
                errorMessage = ex.getMessage();
            }});
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RegisterResponse(){{
                isError = true;
                errorMessage = ex.getMessage();
            }});
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
