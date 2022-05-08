package com.foodspider.controller;

import com.foodspider.exception.InvalidUserException;
import com.foodspider.model.Administrator;
import com.foodspider.model.UserBase;
import com.foodspider.model.narrowed_model.NarrowedUser;
import com.foodspider.model.request_model.LoginRequest;
import com.foodspider.model.request_model.RegisterRequest;
import com.foodspider.model.response_model.LoginResponse;
import com.foodspider.model.response_model.ResponseBase;
import com.foodspider.service.UserBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController extends ControllerBase {

    @Autowired
    private UserBaseService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseBase> login(@RequestBody LoginRequest loginRequest) {
        UserBase user;
        try {
            LOGGER.debug("Logging in user...");
            user = userService.tryLogin(loginRequest.username, loginRequest.password);
        } catch (InvalidUserException ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        LOGGER.info("Successfully logged in user " + loginRequest.username);
        return createOKResponse(new LoginResponse(){{
            isAdmin = user instanceof Administrator;
            userBase = new NarrowedUser(){{
                id = user.getId();
                emailAddress = user.getEmailAddress();
                firstName = user.getFirstName();
                lastName = user.getLastName();
                username = user.getUsername();
                password = user.getPassword();
            }};
        }});
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseBase> register(@RequestBody RegisterRequest registerRequest) {
        try {
            LOGGER.debug("registering new user...");
            userService.tryRegister(registerRequest.emailAddress, registerRequest.firstName,
                    registerRequest.lastName, registerRequest.username, registerRequest.password, registerRequest.isAdmin);
        } catch (InvalidUserException ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        LOGGER.info("Successfully registered user " + registerRequest.username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
