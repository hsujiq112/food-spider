package com.foodspider.controller;

import com.foodspider.model.User;
import com.foodspider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Object> get() {
        var users = new ArrayList<User>();
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
