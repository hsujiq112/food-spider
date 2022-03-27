package com.foodspider.exception;

public class InvalidFoodItemException extends RuntimeException{
    public InvalidFoodItemException(String message) {
        super(message);
    }
}
