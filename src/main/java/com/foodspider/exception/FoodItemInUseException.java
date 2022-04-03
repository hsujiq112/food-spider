package com.foodspider.exception;

public class FoodItemInUseException extends RuntimeException{
    public FoodItemInUseException(String message) {
        super(message);
    }
}
