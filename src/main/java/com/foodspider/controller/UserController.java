package com.foodspider.controller;

import com.foodspider.model.UserBase;
import com.foodspider.service.UserBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

// used mainly for testing... no real use-case found for web app
@RestController
public class UserController {

    @Autowired
    @Qualifier("userService")
    private UserBaseService userService;

    @GetMapping("/users")
    public ResponseEntity<Object> get() {
        var users = new ArrayList<UserBase>();
        try {
             users = userService.dbSet();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        try {
            var userID = UUID.fromString(id);
            userService.deleteByID(userID);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted the user");
    }

}
